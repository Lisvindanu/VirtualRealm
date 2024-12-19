package com.virtualrealm.our.gameMarketPlaces.service.impl

import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPFile
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream

@Service
class FtpService {

    // Metode untuk menguji login FTP
    fun testLogin(server: String, port: Int, username: String, password: String): Boolean {
        val ftpClient = FTPClient()
        return try {
            ftpClient.connect(server, port)
            val loginResult = ftpClient.login(username, password)
            ftpClient.logout()
            loginResult
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            if (ftpClient.isConnected) {
                try {
                    ftpClient.disconnect()
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
    }

    fun listFilesInDirectory(server: String, port: Int, username: String, password: String, directory: String): List<String> {
        val ftpClient = FTPClient()
        val fileList = mutableListOf<String>()
        return try {
            ftpClient.connect(server, port)
            val loginResult = ftpClient.login(username, password)
            if (loginResult) {
                println("Login successful")
                ftpClient.changeWorkingDirectory(directory)
                println("Changed working directory to: $directory")
                val files = ftpClient.listNames()
                if (files != null && files.isNotEmpty()) {
                    println("Files found: ${files.joinToString(", ")}")
                    fileList.addAll(files)
                } else {
                    println("No files found in the directory")
                }
                ftpClient.logout()
            } else {
                println("Login failed: ${ftpClient.replyString}")
            }
            fileList
        } catch (e: Exception) {
            e.printStackTrace()
            println("FTP Error: ${e.message}")
            fileList
        } finally {
            if (ftpClient.isConnected) {
                try {
                    ftpClient.disconnect()
                    println("Disconnected from FTP server.")
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    println("Error closing FTP connection: ${ex.message}")
                }
            }
        }
    }



    fun uploadFileToFtp(
        server: String,
        port: Int,
        username: String,
        password: String,
        file: MultipartFile,
        remoteFilePath: String
    ): Boolean {
        val ftpClient = FTPClient()
        println("=== Start FTP Upload Process ===")

        return try {
            println("Connecting to FTP server: $server on port: $port")
            ftpClient.connect(server, port)

            println("Logging in with username: $username")
            val loginResult = ftpClient.login(username, password)
            if (!loginResult) {
                println("Login failed: ${ftpClient.replyString}")
                return false
            }
            println("Login successful!")

            ftpClient.enterLocalActiveMode()
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE)

            // Set working directory directly to the target path
            val targetDirectory = "domains/virtual-realm.my.id/public_html/uploads/images"
            val directoryExists = ftpClient.changeWorkingDirectory(targetDirectory)

            if (!directoryExists) {
                println("Target directory $targetDirectory does not exist.")
                return false
            }

            println("Changed working directory to: $targetDirectory")

            // Ambil nama file dari remoteFilePath
            val fileName = remoteFilePath.substringAfterLast('/')
            println("Preparing to upload file: $fileName")

            // Upload file
            val inputStream = ByteArrayInputStream(file.bytes)
            val result = ftpClient.storeFile(fileName, inputStream)
            inputStream.close()

            if (result) {
                println("File uploaded successfully!")
            } else {
                println("Upload failed: ${ftpClient.replyString}")
            }

            ftpClient.logout()
            println("Disconnected from FTP server.")
            result
        } catch (e: Exception) {
            e.printStackTrace()
            println("FTP Error: ${e.message}")
            false
        } finally {
            if (ftpClient.isConnected) {
                try {
                    ftpClient.disconnect()
                    println("FTP connection closed.")
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    println("Error closing FTP connection: ${ex.message}")
                }
            }
        }
    }


    fun listDirectoriesInDirectory(server: String, port: Int, username: String, password: String, directory: String): List<String> {
        val ftpClient = FTPClient()
        val directoryList = mutableListOf<String>()
        return try {
            ftpClient.connect(server, port)
            val loginResult = ftpClient.login(username, password)
            if (loginResult) {
                println("Login successful")
                ftpClient.changeWorkingDirectory(directory)
                println("Changed working directory to: $directory")

                // Ambil daftar file dan direktori
                val files: Array<FTPFile> = ftpClient.listFiles()
                for (file in files) {
                    // Cek apakah itu direktori
                    if (file.isDirectory) {
                        directoryList.add(file.name)
                    }
                }

                if (directoryList.isNotEmpty()) {
                    println("Directories found: ${directoryList.joinToString(", ")}")
                } else {
                    println("No directories found in the directory")
                }
                ftpClient.logout()
            } else {
                println("Login failed: ${ftpClient.replyString}")
            }
            directoryList
        } catch (e: Exception) {
            e.printStackTrace()
            println("FTP Error: ${e.message}")
            directoryList
        } finally {
            if (ftpClient.isConnected) {
                try {
                    ftpClient.disconnect()
                    println("Disconnected from FTP server.")
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    println("Error closing FTP connection: ${ex.message}")
                }
            }
        }
    }
}


//package com.virtualrealm.our.gameMarketPlaces.service.impl
//
//import org.apache.commons.net.ftp.FTP
//import org.apache.commons.net.ftp.FTPClient
//import org.apache.commons.net.ftp.FTPFile
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.stereotype.Service
//import org.springframework.web.multipart.MultipartFile
//import java.io.ByteArrayInputStream
//
//@Service
//class FtpService {
//
//    @Value("\${ftp.server}")
//    private lateinit var server: String
//
//    @Value("\${ftp.port}")
//    private var port: Int = 21
//
//    @Value("\${ftp.username}")
//    private lateinit var username: String
//
//    @Value("\${ftp.password}")
//    private lateinit var password: String
//
//    // Metode untuk menguji login FTP
//    fun testLogin(): Boolean {
//        val ftpClient = FTPClient()
//        return try {
//            ftpClient.connect(server, port)
//            val loginResult = ftpClient.login(username, password)
//            ftpClient.logout()
//            loginResult
//        } catch (e: Exception) {
//            e.printStackTrace()
//            false
//        } finally {
//            if (ftpClient.isConnected) {
//                try {
//                    ftpClient.disconnect()
//                } catch (ex: Exception) {
//                    ex.printStackTrace()
//                }
//            }
//        }
//    }
//
//    // Metode untuk listing file di direktori tertentu
//    fun listFilesInDirectory(directory: String): List<String> {
//        val ftpClient = FTPClient()
//        val fileList = mutableListOf<String>()
//        return try {
//            ftpClient.connect(server, port)
//            val loginResult = ftpClient.login(username, password)
//            if (loginResult) {
//                ftpClient.changeWorkingDirectory(directory)
//                val files = ftpClient.listNames()
//                if (files != null) {
//                    fileList.addAll(files)
//                }
//                ftpClient.logout()
//            }
//            fileList
//        } catch (e: Exception) {
//            e.printStackTrace()
//            fileList
//        } finally {
//            if (ftpClient.isConnected) {
//                try {
//                    ftpClient.disconnect()
//                } catch (ex: Exception) {
//                    ex.printStackTrace()
//                }
//            }
//        }
//    }
//
//    // Metode untuk listing direktori di dalam direktori tertentu
//    fun listDirectoriesInDirectory(directory: String): List<String> {
//        val ftpClient = FTPClient()
//        val directoryList = mutableListOf<String>()
//        return try {
//            ftpClient.connect(server, port)
//            val loginResult = ftpClient.login(username, password)
//            if (loginResult) {
//                ftpClient.changeWorkingDirectory(directory)
//                val files: Array<FTPFile> = ftpClient.listFiles()
//                for (file in files) {
//                    if (file.isDirectory) {
//                        directoryList.add(file.name)
//                    }
//                }
//                ftpClient.logout()
//            }
//            directoryList
//        } catch (e: Exception) {
//            e.printStackTrace()
//            directoryList
//        } finally {
//            if (ftpClient.isConnected) {
//                try {
//                    ftpClient.disconnect()
//                } catch (ex: Exception) {
//                    ex.printStackTrace()
//                }
//            }
//        }
//    }
//
//    // Metode untuk meng-upload file ke FTP
//    fun uploadFileToFtp(file: MultipartFile, remoteFilePath: String): Boolean {
//        val ftpClient = FTPClient()
//        return try {
//            ftpClient.connect(server, port)
//            val loginResult = ftpClient.login(username, password)
//            if (!loginResult) return false
//
//            ftpClient.enterLocalActiveMode()
//            ftpClient.setFileType(FTP.BINARY_FILE_TYPE)
//
//            val targetDirectory = "domains/virtual-realm.my.id/public_html/uploads/images"
//            val directoryExists = ftpClient.changeWorkingDirectory(targetDirectory)
//            if (!directoryExists) return false
//
//            val fileName = remoteFilePath.substringAfterLast('/')
//            val inputStream = ByteArrayInputStream(file.bytes)
//            val result = ftpClient.storeFile(fileName, inputStream)
//            inputStream.close()
//
//            ftpClient.logout()
//            result
//        } catch (e: Exception) {
//            e.printStackTrace()
//            false
//        } finally {
//            if (ftpClient.isConnected) {
//                try {
//                    ftpClient.disconnect()
//                } catch (ex: Exception) {
//                    ex.printStackTrace()
//                }
//            }
//        }
//    }
//}
