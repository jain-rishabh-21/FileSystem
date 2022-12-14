package com.demo.scala.files

import com.demo.scala.filesystem.FileSystemException

import scala.annotation.tailrec

class Directory(override val parentPath: String, override val name: String, val contents: List[DirEntry])
  extends DirEntry(parentPath, name ) {

  def hasEntry(name: String): Boolean =
    findEntry(name) != null

  def getAllFoldersInPath: List[String] = {
    // /a/b/c/d => [a,b,c,d]
    path.substring(1).split(Directory.SEPARATOR).toList.filter(x => x.nonEmpty)
  }

  def findDescendents(path: List[String]): Directory = {
    if (path.isEmpty) this
    else findEntry(path.head).asDirectory.findDescendents(path.tail)
  }

  def findDescendents(relativePath: String): Directory = {
    if(relativePath.isEmpty) this
    else findDescendents(relativePath.split(Directory.SEPARATOR).toList)
  }

  def addEntry(newEntry: DirEntry): Directory =
    new Directory(parentPath, name, contents :+ newEntry)

  def findEntry(entryName: String): DirEntry = {
    @tailrec
    def findEntryHelper(name: String, contentList: List[DirEntry]): DirEntry = {
      if (contentList.isEmpty) null
      else if (contentList.head.name.equals(name)) contentList.head
      else findEntryHelper(name, contentList.tail)
    }
    findEntryHelper(entryName, contents)
  }

  def replaceEntry(entryName: String, newEntry: DirEntry): Directory =
    new Directory(parentPath, name, contents.filter(e => !e.name.equals(entryName)) :+ newEntry)

  def removeEntry(entryName: String): Directory = {
    if (!hasEntry(entryName)) this
    else new Directory(parentPath, name, contents.filter(x => !x.name.equals(entryName)))
  }

  def isRoot: Boolean = parentPath.isEmpty

  override def asDirectory: Directory = this

  override def getType: String = "Directory"

  override def asFile: File = throw new FileSystemException("A Directory cannot be converted to Directory")

  override def isDirectory: Boolean = true

  override def isFile: Boolean = false

}

object Directory {
  val SEPARATOR = "/"
  val ROOT_PATH = "/"

  def empty(parentPath: String, name: String): Directory =
    new Directory(parentPath, name, List.empty)

  def ROOT: Directory = Directory.empty("","")
}