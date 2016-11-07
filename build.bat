IF EXIST "build" ( 
  echo Removing build folder
  RMDIR "build" /S /Q
) ELSE (
  echo Build folder does not exist, ignoring...
)
echo Building mod...
gradlew.bat build