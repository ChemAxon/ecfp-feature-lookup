/*
 * Copyright 2016 ChemAxon Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

 package com.chemaxon.examples;

import chemaxon.struc.Molecule;
import com.beust.jcommander.Parameter;

/**
 * CLI parameters.
 *
 * @author Gabor Imre
 */
public final class EcfpFeatureLookupParameters {

    @Parameter(names = "-h", help = true, description = "Print command line help and exit.")
    public boolean help = false;

    @Parameter(names = "-idname", description = "Use molecule name as an ID. Use read count by default.")
    public boolean idname = false;

    @Parameter(names = "-idprop", description = "Use specified molecule property as an ID. Use read count by default.")
    public String idprop = null;

    @Parameter(names = "-c", description = "Custom ECFP config file to use.")
    public String cfgfile = null;

    /**
     * Extract/generate ID based on current settings.
     *
     * @param count Read count
     * @param m Read molecule
     * @return ID to use
     */
    public String id(int count, Molecule m) {
        if (this.idname) {
            return m.getName();
        } else if (this.idprop != null) {
            return m.getProperty(this.idprop);
        } else {
            return Integer.toString(count);
        }
    }
}
