#ImgFinder
This software can be used to search for images on you local machine that contain a certain string. It is using SQLite for caching image contents, Tesseract
for OCR and OpenCV for preprocessing ofimages so OCR can detect text more easily.
The Project uses Maven for dependencies and building.


##License
Note that this code is licensed under the GNU General Public License v3.
If you make any changes to the source you are obliged to also license it under the GPLv3 and you are also required to *release the full source code aswell*.
This may only seem fair, but too many people are just too immature so I have to license it under the GPLv3 copyleft license.


##Building
To get this code up and running on your system you simply have to open the folder in Eclipse and build via Maven. Note that you may have to download the
OpenCV dynamic libraries manually and place the `opencv_java249.dll` in the root directory of the project. It is cruicial to use the OpenCV version 2.4.9.
