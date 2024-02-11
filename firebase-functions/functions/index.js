/**
 * Initiate a recursive delete of documents at a given path.
 *
 * The calling user must be authenticated and have the custom "admin" attribute
 * set to true on the auth token.
 *
 * This delete is NOT an atomic operation and it's possible
 * that it may fail after only deleting some documents.
 *
 * @param {string} data.path the document or collection path to delete.
 */

const functions = require("firebase-functions");
const firebaseTools = require("firebase-tools");

exports.recursiveDelete = functions
    .runWith({
      timeoutSeconds: 540,
      memory: "1GB",
    })
    .https.onCall(async (data, context) => {
    // Only allow admin users to execute this function.
      if (!(data.token)) {
        throw new functions.https.HttpsError(
            "permission-denied",
            "Must be an user with token to initiate delete.",
        );
      }

      const path = data.path;
      console.log(
          `User ${data.token.uid} has requested to delete path ${path}`,
      );

      // Run a recursive delete on the given document or collection path.
      // The 'token' must be set in the functions config, and can be generated
      // at the command line by running 'firebase login:ci'.
      await firebaseTools.firestore
          .delete(path, {
            project: process.env.GCLOUD_PROJECT,
            recursive: true,
            force: true,
            token: data.token,
          });

      return {
        path: path,
      };
    });
