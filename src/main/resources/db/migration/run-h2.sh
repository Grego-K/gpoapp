#!/bin/bash

echo "[PharmaGPO] Ξεκινάει η διαδικασία αυτόματης εκτέλεσης..."
chmod +x gradlew

echo "[PharmaGPO] Καθαρισμός και Build (παράκαμψη tests TBF)..."
./gradlew build -x test

if [ $? -ne 0 ]; then
    echo "[ERROR] Το build απέτυχε. Βεβαιωθείτε ότι έχετε εγκατεστημένη τη Java 21."
    exit 1
fi

echo "[PharmaGPO] Εκτέλεση εφαρμογής με προφίλ H2 (In-Memory DB)..."
echo "[PharmaGPO] Η εφαρμογή θα είναι διαθέσιμη στο: http://localhost:8080"
echo "[PharmaGPO] Για να σταματήσετε την εφαρμογή, πατήστε Ctrl+C."

./gradlew bootRun --args="--spring.profiles.active=h2"