start cmd.exe /c "java -jar target\player-1.0-SNAPSHOT-full.jar player1"
timeout 5
start cmd.exe /c "java -jar target\player-1.0-SNAPSHOT-full.jar initiator player1 TEST_MESSAGE 5"
