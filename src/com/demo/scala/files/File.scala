package com.demo.scala.files

import com.demo.scala.filesystem.FileSystemException

class File(override val parentPath: String, override val name: String, contents: String)
  extends DirEntry(parentPath, name){
  override def asDirectory: Directory = throw new FileSystemException("A file cannot be converted to Directory!")

  override def getType: String = "File"

  override def asFile: File = this

  override def isDirectory: Boolean = false

  override def isFile: Boolean = true

}

object File {
  def empty(parentPath: String, name: String): File =
    new File(parentPath , name, "")
}
