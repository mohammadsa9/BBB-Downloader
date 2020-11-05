package de.maikmerten.bbbdownload.ui;

import de.maikmerten.bbbdownload.downloader.Downloader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.swing.SwingUtilities;

/**
 *
 * @author maik
 */
public class DownloadThread extends Thread {

    private final DownloaderFrame frame;
    private final String url;
    private final String filename;
    private final boolean skipChat;
    private final boolean anonymizeChat;

    public DownloadThread(DownloaderFrame frame, String url, String filename, boolean skipChat, boolean anonymizeChat) {
        this.frame = frame;
        this.url = url;
        this.filename = filename + ".zip";
        this.skipChat = skipChat;
        this.anonymizeChat = anonymizeChat;
    }

    public void unzip(String zipFilePath, String destDir) {
        File dir = new File(destDir);
        // create output directory if it doesn't exist
        if (!dir.exists())
            dir.mkdirs();
        FileInputStream fis;
        // buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while (ze != null) {
                String fileName = ze.getName();
                File newFile = new File(destDir + File.separator + fileName);
                System.out.println("Unzipping to " + newFile.getAbsolutePath());
                // create directories for sub directories in zip
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                // close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            // close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(new File(filename)));

            Downloader d = new Downloader(url, skipChat, anonymizeChat);
            d.downloadPresentation(zos);
            zos.close();
            unzip(filename, filename.substring(0, filename.lastIndexOf('.')));
            File f = new File(filename);
            f.delete();
        } catch (Exception e) {
            // sing and dance, ignore error
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame.setIdle(true);
            }
        });

    }
}
