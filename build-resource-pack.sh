#!/usr/bin/bash
VERSION="{{{VERSION}}}"



NAME=""
while [ $# -gt 0 ]; do
	case "$1" in
	-N|--name)    \shift ;    NAME="$1"      ;;
	--name=*)                 NAME="${1#*=}" ;;
	-V|--version) \shift ; VERSION="$1"      ;;
	--version=*)           VERSION="${1#*=}" ;;
	*) echo "Unknown argument: $1" ; exit 1 ;;
	esac
	\shift
done

if [[ -z $NAME ]]; then
	echo "Resource pack name not provided"
	exit 1
fi
NAME="-${NAME}"

if [[ -z $VERSION ]] || [[ "$VERSION" == "{""{""{VERSION}""}""}" ]]; then
	VERSION=""
else
	VERSION="-${VERSION}"
fi

# remove old resource packs
\ls "./MoreFoods-resourcepack${NAME}"*.zip 2>/dev/null
if [[ $? -eq 0 ]]; then
	\rm -fv --preserve-root  "./MoreFoods-resourcepack${NAME}"*.zip  || exit 1
fi
\ls "./MoreFoods-resourcepack${NAME}"*.sha1 2>/dev/null
if [[ $? -eq 0 ]]; then
	\rm -fv --preserve-root  "./MoreFoods-resourcepack${NAME}"*.sha1  || exit 1
fi
if [[ -f "./plugin/resources/MoreFoods-resourcepack${NAME}.zip" ]]; then
	\rm -fv --preserve-root  "./plugin/resources/MoreFoods-resourcepack${NAME}.zip"  || exit 1
fi
if [[ -f "./plugin/resources/MoreFoods-resourcepack${NAME}.sha1" ]]; then
	\rm -fv --preserve-root  "./plugin/resources/MoreFoods-resourcepack${NAME}.sha1"  || exit 1
fi



# common files
\pushd  "resourcepack/"  >/dev/null  || exit 1
	\zip -r -9  "../MoreFoods-resourcepack${NAME}${VERSION}.zip"  *  || exit 1
\popd >/dev/null

# named files
\pushd  "resourcepack${NAME}/"  >/dev/null  || exit 1
	\zip -r -9  "../MoreFoods-resourcepack${NAME}${VERSION}.zip"  *  || exit 1
\popd >/dev/null



\sha1sum  "MoreFoods-resourcepack${NAME}${VERSION}.zip" \
	> "MoreFoods-resourcepack${NAME}${VERSION}.sha1"  || exit 1

\cp  "MoreFoods-resourcepack${NAME}${VERSION}.zip"   "plugin/resources/MoreFoods-resourcepack${NAME}.zip"
\cp  "MoreFoods-resourcepack${NAME}${VERSION}.sha1"  "plugin/resources/MoreFoods-resourcepack${NAME}.sha1"
