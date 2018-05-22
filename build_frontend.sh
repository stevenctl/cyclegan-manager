#!/usr/bin/env bash
PROFILE=$1
if [ -z PROFILE ]
then
    PROFILE="dev"
fi

cd frontend
npm install

NODE_ENV=$PROFILE brunch build