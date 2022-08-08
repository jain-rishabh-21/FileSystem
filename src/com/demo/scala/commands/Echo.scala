package com.demo.scala.commands
import com.demo.scala.files.{Directory, File}
import com.demo.scala.filesystem.State

import scala.annotation.tailrec

class Echo(args: Array[String]) extends Command {

  def createContent(args: Array[String], topIndex: Int): String = {

    @tailrec
    def createContentHelper(currentIndex: Int, accumulator: String): String= {
      if (currentIndex >= topIndex) accumulator
      else createContentHelper(currentIndex + 1, accumulator + " " + args(currentIndex))
    }
    createContentHelper(0, "")
  }

  def getRootAfterEcho(currentDirectory: Directory, path: List[String], contents: String, append: Boolean): Directory = {
    if(path.isEmpty) currentDirectory
    else if (path.tail.isEmpty) {
      val dirEntry = currentDirectory.findEntry(path.head)
      if(dirEntry == null) currentDirectory.addEntry(new File(currentDirectory.path, path.head, contents))
      else {
        if(dirEntry.isDirectory) currentDirectory
        else
          if (append)  currentDirectory.replaceEntry(path.head, dirEntry.asFile.appendContents(contents))
          else currentDirectory.replaceEntry(path.head, dirEntry.asFile.setContents(contents))
      }
    }
    else {
      val nextDirectory: Directory = currentDirectory.findEntry(path.head).asDirectory
      val newNextDirectory = getRootAfterEcho(nextDirectory, path.tail, contents, append)
      if (newNextDirectory == nextDirectory) currentDirectory
      else currentDirectory.replaceEntry(path.head, newNextDirectory)
    }
  }

  def doEcho(state: State, contents: String, fileName: String, append: Boolean) = {
    if (fileName.contains(Directory.SEPARATOR))
      state.setMessage("ECHO - Should nor contain / !")
    else {
      val newRoot: Directory = getRootAfterEcho(state.root, state.wd.getAllFoldersInPath :+ fileName, contents, append)
      if (newRoot == state.root)
        state.setMessage(fileName + ": No Such File!")
      else
        State(newRoot, newRoot.findDescendents(state.wd.getAllFoldersInPath))
    }
  }

  override def apply(state: State): State = {
    if (args.isEmpty) state
    else if (args.length == 1) state.setMessage(args(0))
    else{
      val operator = args(args.length - 2)
      val fileName = args(args.length - 1)
      val contents = createContent(args, args.length - 2)

      // > echo to a file and make a new file if not there

      // >> appends to the file
      if (operator.equals(">>"))
        doEcho(state, contents, fileName, append = true)
      else if(operator.equals(">"))
        doEcho(state, contents, fileName, append = false)
      else state.setMessage(createContent(args, args.length))

    }
  }
}
