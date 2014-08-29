M3U8Transfer
============

Copies songs in a M3U8 file with their folder structure

#####About
I needed to copy many mp3 files into a memory card and I wanted to have the same folder structure in the memory card. I coded this simple project for myself and then decided to add the GUI so that it might benefit other people who may want the same functionality.

#####Usage
The user interface allows you to select a M3U8 file, a source folder, and a target folder. You can also select which method to copy the files. The label (the box next to the 'Copy from bottom' radio button) which accepts integers is used when the 'Copy from bottom' is selected. When selecting folders and files remember that you have to actually select them instead of just highlighting them.

M3T has two methods of copying files, named 'copy contents' and 'copy from bottom'. M3T requires the songs in the M3U8 file to have absolute paths.

'Copy contents' only copies files that are in the selected source folder. Files that do not have this folder in their path will be skipped. Files that do have this folder will be copied with all their folder structure intact starting from the source folder. The source folder itself is included too. You need to at least select a M3U8 file and a source folder to start copying with this method. 

'Copy from bottom' will copy all songs in the playlist but not necessarily all of the folder structure. The number in the label will be used to determine how many subfolders starting from the bottom will be copied for each song. If the given number is bigger than the number of subfolders for a given song, the maximum number of folders will be copied for that song. You need to select a M3U8 file, a source folder and a target folder to start copying with this method.

When you start copying, the program will show its progress through the text area. When all the files are processed, the line numbers of songs that were not copied will be added to the text area. If you don't see any errors, than all songs were copied successfully.

#####Issues
I am unable to test M3U8 in multiple platforms or under a plethora of circumstances. It works as I expected for the limited usage that I aimed for.

If you encounter an error please let me know and try to provide as much detail as possible. I'll do my best to fix the problem.