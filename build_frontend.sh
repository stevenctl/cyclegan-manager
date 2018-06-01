#!/usr/bin/env bash
cd frontend

npm install
npm run build

cd ..

rm -r src/main/resources/static
mkdir src/main/resources/static
cp frontend/index.html src/main/resources/static
cp -r frontend/public src/main/resources/static
cp -r frontend/dist src/main/resources/static