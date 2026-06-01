const { onValueCreated, onValueUpdated } = require("firebase-functions/database");
const { logger } = require("firebase-functions");
const admin = require("firebase-admin");

admin.initializeApp();

exports.enviarNotificacionMensaje = onValueCreated(
  "/mensajes/{idChat}/{idMensaje}",
  async (event) => {
    const mensaje = event.data.val();

    const idChat = event.params.idChat;
    const idMensaje = event.params.idMensaje;

    logger.log("Nuevo mensaje detectado", {
      idChat,
      idMensaje,
      mensaje,
    });

    if (!mensaje) {
      logger.log("No hay datos en el mensaje");
      return null;
    }

    const idEmisor = mensaje.idEmisor || "";
    const nombreEmisor = mensaje.nombreEmisor || "Nuevo mensaje";
    const texto = mensaje.texto || "Te enviaron un mensaje";

    if (!idEmisor || !idChat) {
      logger.log("Falta idEmisor o idChat", {
        idEmisor,
        idChat,
      });
      return null;
    }

    const db = admin.database();

    const chatEmisorSnapshot = await db
      .ref(`chatsPorUsuario/${idEmisor}/${idChat}`)
      .get();

    if (!chatEmisorSnapshot.exists()) {
      logger.log("No existe el chat del emisor", {
        ruta: `chatsPorUsuario/${idEmisor}/${idChat}`,
      });
      return null;
    }

    const chatEmisor = chatEmisorSnapshot.val();
    const idReceptor = chatEmisor.idOtroUsuario || "";

    if (!idReceptor) {
      logger.log("No se pudo encontrar idReceptor");
      return null;
    }

    logger.log("Receptor encontrado", {
      idReceptor,
    });

    const tokensSnapshot = await db
      .ref(`tokensFCM/${idReceptor}`)
      .get();

    if (!tokensSnapshot.exists()) {
      logger.log("El receptor no tiene tokens FCM guardados", {
        idReceptor,
      });
      return null;
    }

    const tokensObjeto = tokensSnapshot.val();
    const tokens = Object.keys(tokensObjeto || {});

    if (tokens.length === 0) {
      logger.log("Lista de tokens vacía", {
        idReceptor,
      });
      return null;
    }

    logger.log("Tokens encontrados", {
      cantidad: tokens.length,
    });

    const payload = {
      tokens: tokens,
      data: {
        tipo: "chat",
        idChat: String(idChat),
        nombre: String(nombreEmisor),
        texto: String(texto),
      },
      android: {
        priority: "high",
      },
    };

    const respuesta = await admin.messaging().sendEachForMulticast(payload);

    logger.log("Notificación enviada", {
      successCount: respuesta.successCount,
      failureCount: respuesta.failureCount,
    });

    return null;
  }
);

exports.enviarNotificacionTrote = onValueUpdated(
  "/ubicacionUsuario/{idDeportista}/disponible",
  async (event) => {
    const disponibleAntes = event.data.before.val();
    const disponibleAhora = event.data.after.val();

    const idDeportista = event.params.idDeportista;

    logger.log("Cambio en disponibilidad detectado", {
      idDeportista,
      disponibleAntes,
      disponibleAhora,
    });

    if (disponibleAntes === true || disponibleAhora !== true) {
      logger.log("No es inicio de trote, no se envía notificación");
      return null;
    }

    const db = admin.database();

    const ubicacionSnapshot = await db
      .ref(`ubicacionUsuario/${idDeportista}`)
      .get();

    if (!ubicacionSnapshot.exists()) {
      logger.log("No existe ubicación del deportista", {
        idDeportista,
      });
      return null;
    }

    const ubicacion = ubicacionSnapshot.val();

    const nombreDeportista = ubicacion.nombre || "Un deportista";
    const tipoActividad = ubicacion.tipoActividad || "actividad";

    const deportistaSnapshot = await db
      .ref(`deportistas/${idDeportista}/entrenadores`)
      .get();

    if (!deportistaSnapshot.exists()) {
      logger.log("El deportista no tiene entrenadores asociados", {
        idDeportista,
      });
      return null;
    }

    const entrenadoresObjeto = deportistaSnapshot.val();
    const idsEntrenadores = Object.values(entrenadoresObjeto || {});

    if (idsEntrenadores.length === 0) {
      logger.log("Lista de entrenadores vacía", {
        idDeportista,
      });
      return null;
    }

    const tokens = [];

    for (const idEntrenador of idsEntrenadores) {
      const tokensSnapshot = await db
        .ref(`tokensFCM/${idEntrenador}`)
        .get();

      if (tokensSnapshot.exists()) {
        const tokensObjeto = tokensSnapshot.val();
        const tokensEntrenador = Object.keys(tokensObjeto || {});
        tokens.push(...tokensEntrenador);
      }
    }

    if (tokens.length === 0) {
      logger.log("No hay tokens FCM para los entrenadores", {
        idsEntrenadores,
      });
      return null;
    }

    const texto = `${nombreDeportista} inició ${tipoActividad}, ¿quieres ver dónde está?`;

    const payload = {
      tokens: tokens,
      data: {
        tipo: "trote",
        idDeportista: String(idDeportista),
        nombreDeportista: String(nombreDeportista),
        tipoActividad: String(tipoActividad),
        texto: String(texto),
      },
      android: {
        priority: "high",
      },
    };

    const respuesta = await admin.messaging().sendEachForMulticast(payload);

    logger.log("Notificación de trote enviada", {
      successCount: respuesta.successCount,
      failureCount: respuesta.failureCount,
      cantidadTokens: tokens.length,
    });

    return null;
  }
);