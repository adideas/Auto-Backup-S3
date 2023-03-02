# Auto Backup S3

Amazon database and folder backup.

> Author: [Alexey Vlasov](https://github.com/adideas) </br>
> [LICENSE](LICENSE)
 
## Depends

|                                                                     | NAME                    | LINK                                                                                                          | License                                                |
|---------------------------------------------------------------------|-------------------------|---------------------------------------------------------------------------------------------------------------|--------------------------------------------------------|
| ![](https://mvnrepository.com/img/bb8a75bf678ff164a64f90874786b157) | AWS SDK For Java        | [com.amazonaws:aws-java-sdk](https://mvnrepository.com/artifact/com.amazonaws/aws-java-sdk)                   | [Apache 2.0](LICENSES/APACHE 2)                        |
| ![](https://mvnrepository.com/img/8a6a5762fa2fb69b58a6d42fee4ec53f) | JAXB API                | [javax.xml.bind:jaxb-api](https://mvnrepository.com/artifact/javax.xml.bind/jaxb-api)                         | [CDDL 1.1](LICENSES/CDDL 1.1), [GPL 2](LICENSES/GPL 2) |
| ![](https://mvnrepository.com/img/546dde4a7f51b5b6daa64e9f8f58a8bc) | Apache Commons Compress | [org.apache.commons:commons-compress](https://mvnrepository.com/artifact/org.apache.commons/commons-compress) | [Apache 2.0](LICENSES/APACHE 2)                        |
| ![](https://mvnrepository.com/img/e81f4bc5524ca02d3570ecd1fed08c2c) | MySQL Connector Java    | [mysql:mysql-connector-java](https://mvnrepository.com/artifact/mysql/mysql-connector-java)                   | [LICENSE](LICENSES/MYSQL_CONNECTOR_JAVA)               |
| ![](https://mvnrepository.com/img/e94b52a277d4919d43f455e29818c222) | Gson                    | [com.google.code.gson:gson](https://mvnrepository.com/artifact/com.google.code.gson/gson)                     | [Apache 2.0](LICENSES/APACHE 2)                        |
| ![](https://mvnrepository.com/img/a117c37ecc0ed0750c48bd4755638e06) | Apache Commons Lang     | [org.apache.commons:commons-lang3](https://mvnrepository.com/artifact/org.apache.commons/commons-lang3)       | [Apache 2.0](LICENSES/APACHE 2)                        |


## Commands
| Command                        | About                         |
|--------------------------------|-------------------------------|
| help                           | Get help                      |
| make-env                       | Create default env            |
| service                        | Service start                 |
| make-systemd                   | Print example systemd service |
| make-systemd > example.service | Make example systemd service  |

## Install LINUX
### 1) Make service

> /usr/lib/systemd/system/***.service

```bash
[Unit]
# Description process
Description=Description process
After=network.target

[Service]
SuccessExitStatus=143
Type=simple
Restart=on-failure
RestartSec=10

User=user
WorkingDirectory=/
ExecStart=/bin/java -Xms128m -Xmx256m -jar /***.jar service
StandardOutput=file:/system_service_output.log
StandardError=file:/system_service_error.log

[Install]
WantedBy=multi-user.target
```

### 2) Run service
```bash
sudo systemctl daemon-reload
sudo systemctl start ***.service
sudo systemctl enable ***.service
```
