#!/usr/bin/bash


if [[ -f MoreFoods-resourcepack.zip ]]; then
	\rm -fv  MoreFoods-resourcepack.zip  || exit 1
fi


\pushd  "resourcepack/"  >/dev/null  || exit 1
	\zip -r -9  ../MoreFoods-resourcepack.zip *  || exit 1
\popd >/dev/null


\sha1sum MoreFoods-resourcepack.zip > MoreFoods-resourcepack.sha1  || exit 1
