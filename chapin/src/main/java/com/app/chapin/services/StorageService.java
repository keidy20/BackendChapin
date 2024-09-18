package com.app.chapin.services;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${BUCKET_NAME}")
    private String BUCKET_NAME;

    @Value("${FIREBASE_KEY}")
    private String FIREBASE_KEY;

    private String uploadOrUpdateFile(File file, String fileName, String contentType) throws IOException {
        BlobId blobId = BlobId.of(BUCKET_NAME, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(contentType).build();
        InputStream inputStream = StorageService.class.getClassLoader().getResourceAsStream(FIREBASE_KEY);
        Credentials credentials = GoogleCredentials.fromStream(inputStream);
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));
        return generateDownloadUrl(fileName);
    }

    public void eliminarCarpetas() throws IOException {
        InputStream inputStream = StorageService.class.getClassLoader().getResourceAsStream(FIREBASE_KEY); // change the file name with your one
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
        }
        return tempFile;
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }


    public String upload(MultipartFile multipartFile, String path) {
        try {
            String fileName = multipartFile.getOriginalFilename();
            //fileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));  // to generated random string values for file name.

            File file = this.convertToFile(multipartFile, fileName);
            String URL = this.uploadOrUpdateFile(file, path.concat(fileName), multipartFile.getContentType());
            file.delete();
            return URL;
        } catch (Exception e) {
            e.printStackTrace();
            return "Image couldn't upload, Something went wrong";
        }
    }



}
