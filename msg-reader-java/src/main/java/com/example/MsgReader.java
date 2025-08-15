package com.example;

import org.apache.poi.hsmf.MAPIMessage;
import org.apache.poi.hsmf.datatypes.AttachmentChunks;
import org.apache.poi.hsmf.exceptions.ChunkNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MsgReader {

    private static final Logger logger = LoggerFactory.getLogger(MsgReader.class);

    public static void main(String[] args) {
        if (args.length != 1) {
            logger.error("Usage: java -jar msg-reader-1.0-SNAPSHOT.jar <folder_path>");
            return;
        }

        File folder = new File(args[0]);
        if (!folder.isDirectory()) {
            logger.error("The provided path is not a directory.");
            return;
        }

        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".msg"));

        if (files == null || files.length == 0) {
            logger.info("No .msg files found in the directory.");
            return;
        }

        File attachmentsDir = new File("dir/attachments");
        if (!attachmentsDir.exists()) {
            attachmentsDir.mkdirs();
        }

        for (File msgFile : files) {
            logger.info("===================================================");
            logger.info("Reading file: {}", msgFile.getName());
            logger.info("===================================================");
            try (MAPIMessage msg = new MAPIMessage(msgFile)) {
                try {
                    logger.info("From: {}", msg.getDisplayFrom());
                    logger.info("To: {}", msg.getDisplayTo());
                    logger.info("Subject: {}", msg.getSubject());
                    logger.info("Body: {}", msg.getTextBody());
                } catch (ChunkNotFoundException e) {
                    logger.error("Could not extract a specific part of the message (e.g. body, subject). It may be in a different format or missing.", e);
                }

                for (AttachmentChunks attachment : msg.getAttachmentFiles()) {
                    String filename = attachment.getAttachLongFileName().toString();
                    if (filename.isEmpty()) {
                        filename = attachment.getAttachFileName().toString();
                    }

                    File attachmentFile = new File(attachmentsDir, filename);
                    try (FileOutputStream fos = new FileOutputStream(attachmentFile)) {
                        fos.write(attachment.getAttachData().getValue());
                        logger.info("Saved attachment: {}", filename);
                    }
                }

            } catch (IOException e) {
                logger.error("Error reading file {}: {}", msgFile.getName(), e.getMessage(), e);
            }
            logger.info("\n");
        }
    }
}
