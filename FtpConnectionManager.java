package com.api.ftpConnection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class FtpConnectionManager {
	
	private static FTPClient ftpclient;
	private static String ftpLocation = "C:\\Users\\santi\\git\\Ftp_test";
	private static String formatoImagen = ".png";
	
	public static FTPClient getConnection() throws SocketException {
		if (ftpclient == null) {
			ftpclient = new FTPClient();
			try {
				ftpclient.connect("127.0.0.1",21);
				ftpclient.login("test", null);
				
				showServerReply(ftpclient);
				int replyCode = ftpclient.getReplyCode();
				//Si el server falla
				if(!FTPReply.isPositiveCompletion(replyCode)) {
					System.out.println("ERROR SERVER FTP : Operacion fallida: " + replyCode);
				} else {
		            ftpclient.enterLocalPassiveMode();
		            try {
						ftpclient.setFileType(FTP.BINARY_FILE_TYPE);
					} catch (IOException e) {
						System.out.println("no pude setear el tipo de archivo.");
						e.printStackTrace();
					}
				}
				System.out.println("Me conecte al servidor FTP :D");
			} catch (IOException e) {
				System.out.println("No me puedo conectar con el servidor FTP, estan bien los parametros?..");
			}
		}
		return ftpclient;
	}
	
	public static List<String> uploadFileList(List<File> images) {
		ArrayList<String> directions = new ArrayList<String>();
		
		if (images.isEmpty())
			return directions;
		
		if (ftpclient == null) {
			try {
				getConnection();		
			} catch (SocketException e) {
				e.printStackTrace();
			}
		}
		
		String fileFtpName = null;
		InputStream inputStream = null;
		
		for(File file: images) {
			try {
				fileFtpName = HashAlgo.getFileHash(file);
			} catch (NoSuchAlgorithmException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			try {
				inputStream = new FileInputStream(file);
				boolean uploaded = false;
				try {
					System.out.println("Subiendo archivo!");
					uploaded = ftpclient.storeFile(fileFtpName, inputStream);
				} catch (IOException e) {
					System.out.println("error al guardar el archivo en el servidor.");
					e.printStackTrace();
				}
				if(uploaded) {
					System.out.println("El archivo se subio con exito!");
				} else {
					System.out.println("algo fallo");
				}
			} catch (FileNotFoundException e) {
				System.out.println("fallo al crearse el inputstream!.");
				e.printStackTrace();
			} finally {
				try {	
					inputStream.close();
				} catch (IOException e) {
					System.out.println("no se pudo cerrar el inputStream");
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			String fileLocation = new String(ftpLocation + "\\" + fileFtpName);
			directions.add(fileLocation);
		}
		
		return directions;
	}
	
	public static String uploadFile(File file) {
		
		if (ftpclient == null) {
			try {
				getConnection();		
			} catch (SocketException e) {
				e.printStackTrace();
			}
		}
		
		String fileFtpName = null;
		InputStream inputStream = null;
		
		try {
				fileFtpName = HashAlgo.getFileHash(file);
			} catch (NoSuchAlgorithmException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			try {
				inputStream = new FileInputStream(file);
				boolean uploaded = false;
				try {
					System.out.println("Subiendo archivo!");
					uploaded = ftpclient.storeFile(fileFtpName, inputStream);
				} catch (IOException e) {
					System.out.println("error al guardar el archivo en el servidor.");
					e.printStackTrace();
				}
				if(uploaded) {
					System.out.println("El archivo se subio con exito!");
				} else {
					System.out.println("algo fallo");
				}
			} catch (FileNotFoundException e) {
				System.out.println("fallo al crearse el inputstream!.");
				e.printStackTrace();
			} finally {
				try {	
					inputStream.close();
				} catch (IOException e) {
					System.out.println("no se pudo cerrar el inputStream");
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		String fileLocation= new String(ftpLocation + "\\" + fileFtpName + formatoImagen);
		System.out.println(fileLocation);
		return fileLocation;
	}
	
    private static void showServerReply(FTPClient ftpClient) {
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                System.out.println("SERVER: " + aReply);
            }
        }
    }
}
