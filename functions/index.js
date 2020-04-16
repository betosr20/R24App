/**
 * Reference link
 * https://medium.com/@97preveenraj/firebase-cloud-messaging-fcm-with-firebase-realtime-database-7388493cb869
 */

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.newIncidentReport = functions.database.ref('/Report/{id}')
    .onCreate((snapshot, context) => {
        var message = '';

        if (snapshot._data != null && snapshot._data != undefined) {
            message = {
                notification: {
                    title: 'Nuevo reporte de incidente',
                    body: snapshot._data.type + ' en ' + snapshot._data.place
                },

                data: {
                    'reportId': snapshot._data.id
                }
            };

        }

        return admin.messaging().sendToTopic('IncidentAlert', message)
            .then(function (response) {
                console.log("Successfully sent message:", response);
            })
            .catch(function (error) {
                console.log("Error sending message:", error);
            });
    });

    exports.newDistressSignal = functions.database.ref('/DistressSignal/{id}')
        .onCreate((snapshot, context) => {
            var message = '';

            if (snapshot._data != null && snapshot._data != undefined) {
                message = {
                    notification: {
                        title: 'Nueva alerta de auxilio',
                        body:  snapshot._data.name +  ' ' + snapshot._data.lastName + ' ha emitido una señal de auxilio' + ' en ' + snapshot._data.locationPlace
                    },

                    data: {
                        'reportId': snapshot._data.id
                    }
                };

            }

            return admin.messaging().sendToTopic('DistressAlert', message)
                .then(function (response) {
                    console.log("Successfully sent message:", response);
                })
                .catch(function (error) {
                    console.log("Error sending message:", error);
                });
        });