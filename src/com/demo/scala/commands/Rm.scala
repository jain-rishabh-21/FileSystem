package com.demo.scala.commands
import com.demo.scala.files.Directory
import com.demo.scala.filesystem.State

import scala.annotation.tailrec

class Rm(name: String) extends Command {

  def doRm(state: State, path: String): State = {

    def rmHelper(currentDir: Directory, path: List[String]): Directory = {
      if (path.isEmpty) currentDir
      else if (path.tail.isEmpty)  currentDir.removeEntry(path.head)
      else {
        val nextDirectory = currentDir.findEntry(path.head)
        if(!nextDirectory.isDirectory) currentDir
        else {
          val newNextDir = rmHelper(nextDirectory.asDirectory, path.tail)
          if(newNextDir == nextDirectory) currentDir
          else currentDir.replaceEntry(path.head, newNextDir)
        }
      }
    }

    val tokens = path.substring(1).split(Directory.SEPARATOR).toList
    val newRoot: Directory = rmHelper(state.root, tokens)
    if(newRoot == state.root)
      state.setMessage(path + ": No such Directory!")
    else
      State(newRoot, newRoot.findDescendents(state.wd.path.substring(1)))
  }

  override def apply(state: State): State = {
    val workingDir = state.wd
    val absPath = {
      if(name.startsWith(Directory.SEPARATOR)) name
      else if(workingDir.isRoot) workingDir.path + name
      else workingDir.path + Directory.SEPARATOR + name
    }
    if(Directory.ROOT_PATH.equals(absPath))
      state.setMessage("Not Allowed to delete Root!")
    else
      doRm(state, absPath)
  }
}
