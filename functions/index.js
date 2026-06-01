const { onValueCreated } = require("firebase-functions/database");
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