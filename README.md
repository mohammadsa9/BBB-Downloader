# bbb-downloader
A Java-application to download presentations recorded in BigBlueButton. The tool creates folder that contain all necessary files to host the downloaded recordings outside of BigBlueButton. Also the folder contains "View.jar" file to create http server and host the files so you can view the recordings.

![user interface](ui.png)

## Notice on licensing

The code for this project is MIT-licensed. However, note that [src/main/resources/playback.zip](src/main/resources/playback.zip) includes the playback application code from BigBlueButton, which is GPL-licensed, amongst some JavaScript libraries with deviating licenses. Consult the file-headers for the files contained within that archive for licensing information.