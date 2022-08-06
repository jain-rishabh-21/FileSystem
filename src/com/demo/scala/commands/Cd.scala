package com.demo.scala.commands
import com.demo.scala.files.{DirEntry, Directory}
import com.demo.scala.filesystem.State

import scala.annotation.tailrec

class Cd(dir: String) extends Command {

  def doFindEntry(root: Directory, path: String): DirEntry = {
    @tailrec
    def findEntryHelper(currentDirectory: Directory, path: List[String]): DirEntry = {
      if (path.isEmpty || path.head.isEmpty) currentDirectory
      else if (path.tail.isEmpty) currentDirectory.findEntry(path.head)
      else {
        val nextDir = currentDirectory.findEntry(path.head)
        if (nextDir == null || !nextDir.isDirectory) null
        else findEntryHelper(nextDir.asDirectory, path.tail)
      }
    }

    @tailrec
    def collapseRelativeTokens(path: List[String], result: List[String]): List[String] = {
      if(path.isEmpty) result
      else if (".".equals(path.head)) collapseRelativeTokens(path.tail, result)
      else if ("..".equals(path.head)) {
        if (result.isEmpty) null
        else collapseRelativeTokens(path.tail, result.init) // prepending the elements
      } else collapseRelativeTokens(path.tail, result :+ path.head)
    }

    val tokens: List[String] = path.substring(1).split(Directory.SEPARATOR).toList
    val newTokens: List[String] = collapseRelativeTokens(tokens, List.empty)
    if (newTokens == null) null
    else findEntryHelper(root, newTokens)
  }

  override def apply(state: State): State = {

    //Find the root
    val root = state.root
    val wd = state.wd
    //Find the abs path of Dir which need to be cd
    val absolutePath =
      if (dir.startsWith(Directory.SEPARATOR)) dir
      else if (wd.isRoot) wd.path + dir
      else wd.path + Directory.SEPARATOR + dir

    //Find the Dir
    val destinationDir = doFindEntry(root, absolutePath)

    // Change the state given new DIR
    if(destinationDir == null || !destinationDir.isDirectory)
      state.setMessage("No Such Directory!")
    else
      State( root, destinationDir.asDirectory)
  }
}
