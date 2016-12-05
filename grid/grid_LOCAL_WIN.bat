start java -jar selenium-server-standalone.jar -role hub
timeout /t 3
start java -jar selenium-server-standalone.jar -role node -Dwebdriver.ie.driver=drivers\IEDriverServer.exe  -Dwebdriver.chrome.driver=drivers\chromedriver.exe -nodeConfig DefaultNode_LOCAL_WIN.json