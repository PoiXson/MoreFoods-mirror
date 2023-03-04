#!/usr/bin/bash


if [[ -f resources/FoodRot-resourcepack.zip ]]; then
	\rm -fv  resources/FoodRot-resourcepack.zip  || exit 1
fi


\pushd  "resources/pack/"  >/dev/null  || exit 1
	\zip -r -9  ../FoodRot-resourcepack.zip *  || exit 1
\popd >/dev/null


\pushd  "resources/"  >/dev/null  || exit 1
	\sha1sum FoodRot-resourcepack.zip > FoodRot-resourcepack.sha1  || exit 1
\popd >/dev/null
