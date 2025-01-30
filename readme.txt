## ================================================================================
##  More Foods
## Copyright (c) 2013-2025 Mattsoft/PoiXson
## <https://mattsoft.net> <https://poixson.com>
## Released under the AGPL 3.0 + ADD-PXN-V1
##
## Description: Adds more foods and food aging
##
## ================================================================================
##
## This program is free software: you can redistribute it and/or modify it under
## the terms of the GNU Affero General Public License + ADD-PXN-V1 as published by
## the Free Software Foundation, either version 3 of the License, or (at your
## option) any later version, with the addition of ADD-PXN-V1.
##
## This program is distributed in the hope that it will be useful, but WITHOUT ANY
## WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
## PARTICULAR PURPOSE.
##
## See the GNU Affero General Public License for more details
## <http://www.gnu.org/licenses/agpl-3.0.en.html> and Addendum ADD-PXN-V1
## <https://dl.poixson.com/ADD-PXN-V1.txt>
##
## **ADD-PXN-V1 - Addendum to the GNU Affero General Public License (AGPL)**
## This Addendum is an integral part of the GNU Affero General Public License
## (AGPL) under which this software is licensed. By using, modifying, or
## distributing this software, you agree to the following additional terms:
##
## 1. **Source Code Availability:** Any distribution of the software, including
##    modified versions, must include the complete corresponding source code. This
##    includes all modifications made to the original source code.
## 2. **Free of Charge and Accessible:** The source code and any modifications to
##    the source code must be made available to all with reasonable access to the
##    internet, free of charge. No fees may be charged for access to the source
##    code or for the distribution of the software, whether in its original or
##    modified form. The source code must be accessible in a manner that allows
##    users to easily obtain, view, and modify it. This can be achieved by
##    providing a link to a publicly accessible repository (e.g., GitHub, GitLab)
##    or by including the source code directly with the distributed software.
## 3. **Documentation of Changes:** When distributing modified versions of the
##    software, you must provide clear documentation of the changes made to the
##    original source code. This documentation should be included with the source
##    code, and should be easily accessible to users.
## 4. **No Additional Restrictions:** You may not impose any additional
##    restrictions on the rights granted by the AGPL or this Addendum. All
##    recipients of the software must have the same rights to use, modify, and
##    distribute the software as granted under the AGPL and this Addendum.
## 5. **Acceptance of Terms:** By using, modifying, or distributing this software,
##    you acknowledge that you have read, understood, and agree to comply with the
##    terms of the AGPL and this Addendum.
## ================================================================================


MoreFoods - Adds more foods and food aging

This plugin adds more foods and food aging.

time to age one stage = (1.0 / chance ) * interval * delay

The default chance is 0.164 with an interval of 5 minutes and 5 delay cycles per stage, resulting in about one stage every 2.5 hours. A food with 4 stages and 5 delay cycles each would take about 10 hours to fully age and rot. There is a 1 in 6 random chance for a food item to age every 5 minutes.

Requires pxnPluginLib plugin:
https://dev.bukkit.org/projects/pxnpluginlib
or https://www.spigotmc.org/resources/pxnpluginlib.107049/

Discord: https://discord.gg/jHgP2a3kDx

Dev Builds: https://dl.poixson.com/mcplugins/MoreFoods/
