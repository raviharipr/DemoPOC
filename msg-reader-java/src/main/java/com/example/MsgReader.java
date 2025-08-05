package com.example;

import org.apache.poi.hsmf.MAPIMessage;
import org.apache.poi.hsmf.datatypes.AttachmentChunks;
import org.apache.poi.hsmf.exceptions.ChunkNotFoundException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MsgReader {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java -jar msg-reader-1.0-SNAPSHOT.jar <folder_path>");
            args = new String[]{"./dir/sample-msg-files/"};
            //   return;
        }

        File folder = new File(args[0]);
        if (!folder.isDirectory()) {
            System.out.println("The provided path is not a directory.");
            return;
        }

        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".msg"));

        if (files == null || files.length == 0) {
            System.out.println("No .msg files found in the directory.");
            return;
        }

        File attachmentsDir = new File("dir/attachments");
        if (!attachmentsDir.exists()) {
            attachmentsDir.mkdirs();
        }

        for (File msgFile : files) {
            System.out.println("===================================================");
            System.out.println("Reading file: " + msgFile.getName());
            System.out.println("===================================================");
            try (MAPIMessage msg = new MAPIMessage(msgFile)) {
                try {
                    System.out.println("From: " + msg.getDisplayFrom());
                    System.out.println("To: " + msg.getDisplayTo());
                    System.out.println("Subject: " + msg.getSubject());
                    System.out.println("Body: " + msg.getTextBody());
                } catch (ChunkNotFoundException e) {
                    System.err.println("Could not extract a specific part of the message (e.g. body, subject). It may be in a different format or missing.");
                }

                for (AttachmentChunks attachment : msg.getAttachmentFiles()) {
                    String filename = attachment.getAttachLongFileName().toString();
                    if (filename.isEmpty()) {
                        filename = attachment.getAttachFileName().toString();
                    }

                    File attachmentFile = new File(attachmentsDir, filename);
                    try (FileOutputStream fos = new FileOutputStream(attachmentFile)) {
                        fos.write(attachment.getAttachData().getValue());
                        System.out.println("Saved attachment: " + filename);
                    }
                }

            } catch (IOException e) {
                System.err.println("Error reading file " + msgFile.getName() + ": " + e.getMessage());
            }
            System.out.println("\n");
        }
    }
}
