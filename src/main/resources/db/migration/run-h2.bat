@echo off
echo [PharmaGPO] Ξεκινάει η διαδικασία αυτόματης εκτέλεσης...
echo [PharmaGPO] Καθαρισμός και Build ( με παράκαμψη tests TBFixed)...

call gradlew.bat build -x test

if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Το build απέτυχε. Βεβαιωθείτε ότι έχετε εγκατεστημένη τη Java 21.
    pause
    exit /b %ERRORLEVEL%
)

echo [PharmaGPO] Εκτέλεση εφαρμογής με προφίλ H2 (In-Memory DB)...
echo [PharmaGPO] Η εφαρμογή είναι διαθέσιμη στο: http://localhost:8080
echo [PharmaGPO] Για να σταματήσετε την εφαρμογή, πατήστε Ctrl+C.

call gradlew.bat bootRun --args="--spring.profiles.active=h2"
pause