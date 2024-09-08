package com.app.chapin.services;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Service
public class StorageService {

    private final String BUCKET_NAME = "indigo-cider-432618-r6.appspot.com";
    private String uploadOrUpdateFile(File file, String fileName) throws IOException {
        BlobId blobId = BlobId.of("indigo-cider-432618-r6.appspot.com", fileName); // Replace with your bucker name
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("application/pdf").build();
        InputStream inputStream = StorageService.class.getClassLoader().getResourceAsStream("firebase-private-key.json"); // change the file name with your one
        Credentials credentials = GoogleCredentials.fromStream(inputStream);
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));
//storage.get("indigo-cider-432618-r6.appspot.com","test/constanciartu.pdf")
        return generateDownloadUrl(fileName);
    }

    public void eliminarCarpetas() throws IOException {
        InputStream inputStream = StorageService.class.getClassLoader().getResourceAsStream("firebase-private-key.json"); // change the file name with your one
        Credentials credentials = GoogleCredentials.fromStream(inputStream);
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

        // Ruta de la carpeta que queremos eliminar (esto simula la eliminación de carpetas)
        String folderPath = "test1/";             // Lista todos los archivos en la "carpeta"
        //Iterable<Blob> blobs = storage.list(String.valueOf(Storage.BlobListOption.prefix(folderPath))).iterateAll();
        Iterable<Blob> blobs = storage.list(BUCKET_NAME, Storage.BlobListOption.prefix(folderPath)).iterateAll();

// Elimina cada archivo
    for (Blob blob : blobs) {
        blob.delete();
        System.out.println("Archivo eliminado: " + blob.getName()); } System.out.println("Carpeta eliminada correctamente.");
    }

    public Blob getArchivo(String ruta) throws IOException {
        InputStream inputStream = StorageService.class.getClassLoader().getResourceAsStream("firebase-private-key.json"); // change the file name with your one
        Credentials credentials = GoogleCredentials.fromStream(inputStream);
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        return storage.get(BUCKET_NAME, ruta);
    }

    public boolean deleteArchivo(String ruta) throws IOException {
        InputStream inputStream = StorageService.class.getClassLoader().getResourceAsStream("firebase-private-key.json"); // change the file name with your one
        Credentials credentials = GoogleCredentials.fromStream(inputStream);
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        return storage.delete(BUCKET_NAME, ruta);
    }

    private String generateDownloadUrl(String fileName) {
        // URL de descarga pública
        return String.format("https://firebasestorage.googleapis.com/v0/b/%s/o/%s?alt=media", BUCKET_NAME, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }

    private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File tempFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
            fos.close();
        }
        return tempFile;
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }


    public String upload(MultipartFile multipartFile) {
        try {
            String fileName = multipartFile.getOriginalFilename();                        // to get original file name
            //fileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));  // to generated random string values for file name.

            File file = this.convertToFile(multipartFile, fileName);                      // to convert multipartFile to File
            String URL = this.uploadOrUpdateFile(file, "test1/"+fileName);                                   // to get uploaded file link
            file.delete();
            return URL;
        } catch (Exception e) {
            e.printStackTrace();
            return "Image couldn't upload, Something went wrong";
        }
    }



}
