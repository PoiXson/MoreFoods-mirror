#!/usr/bin/bash
VERSION="{{{VERSION}}}"


if [[ -z $VERSION ]] || [[ "$VERSION" == "\{\{\{VERSION\}\}\}" ]]; then
	VERSION=""
else
	VERSION="-${VERSION}"
fi


\ls "./MoreFoods-resourcepack"*.zip >/dev/null 2>/dev/null
if [[ $? -eq 0 ]]; then
	\rm -fv --preserve-root  "./MoreFoods-resourcepack"*.zip  || exit 1
fi
\ls "./MoreFoods-resourcepack"*.sha1 >/dev/null 2>/dev/null
if [[ $? -eq 0 ]]; then
	\rm -fv --preserve-root  "./MoreFoods-resourcepack"*.sha1  || exit 1
fi
if [[ -f "./plugin/resources/MoreFoods-resourcepack.zip" ]]; then
	\rm -fv --preserve-root  "./plugin/resources/MoreFoods-resourcepack.zip"  || exit 1
fi
if [[ -f "./plugin/resources/MoreFoods-resourcepack.sha1" ]]; then
	\rm -fv --preserve-root  "./plugin/resources/MoreFoods-resourcepack.sha1"  || exit 1
fi


\pushd  "resourcepack/"  >/dev/null  || exit 1
	\zip -r -9  "../MoreFoods-resourcepack${VERSION}.zip"  *  || exit 1
\popd >/dev/null


\sha1sum  "MoreFoods-resourcepack${VERSION}.zip" \
	> "MoreFoods-resourcepack${VERSION}.sha1"  || exit 1


\cp  "MoreFoods-resourcepack${VERSION}.zip"   "plugin/resources/MoreFoods-resourcepack.zip"
\cp  "MoreFoods-resourcepack${VERSION}.sha1"  "plugin/resources/MoreFoods-resourcepack.sha1"
