if [ -d "build" ]; then
  echo "Removing build folder"
  rm -rf build
else
  echo "Build folder does not exist, ignoring..."
fi
echo "Building mod..."
gradle build
