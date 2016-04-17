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

import chemaxon.descriptors.ECFP;
import chemaxon.descriptors.ECFPFeature;
import chemaxon.descriptors.ECFPFeatureLookup;
import chemaxon.descriptors.ECFPGenerator;
import chemaxon.descriptors.ECFPParameters;
import chemaxon.descriptors.MDGeneratorException;
import chemaxon.formats.MolExporter;
import chemaxon.formats.MolImporter;
import chemaxon.struc.Molecule;
import com.beust.jcommander.JCommander;
import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * Example to exercise {@link ECFPFeatureLookup} class.
 *
 * <p>The example reads molecules from standard input and prints identified features visualization in SMARTS format
 * to the standard output. Verbose messages are printed to the standard error.</p>
 *
 * <p>Output format: {@code <FEATURE_SMARTS> <MOLECULE_ID> ECFPID: <FEATURE_ID> ECFPBIT: <FEATURE_BIT_POSITION> DIA: <FEATURE_DIAMETER> ATOM: [<CENTRAL_ATOM_INDEX_LIST>]}</p>
 *
 * @see <a href="http://www.chemaxon.com/jchem/doc/dev/java/api/index.html?chemaxon/descriptors/ECFPFeatureLookup.html">http://www.chemaxon.com/jchem/doc/dev/java/api/index.html?chemaxon/descriptors/ECFPFeatureLookup.html</a>
 * @see <a href="http://www.chemaxon.com/jchem/doc/dev/java/api/index.html?chemaxon/descriptors/ECFP.html">http://www.chemaxon.com/jchem/doc/dev/java/api/index.html?chemaxon/descriptors/ECFP.html</a>
 * @see <a href="http://www.chemaxon.com/jchem/doc/user/ECFP.html">http://www.chemaxon.com/jchem/doc/user/ECFP.html</a>
 *
 * @author Gabor Imre
 */
public final class EcfpFeatureLookup {

    /**
     * No public constructor.
     */
    private EcfpFeatureLookup() {}


    /**
     * Look up and print features.
     *
     * @param ecfpParameters ECFP parameters to use
     * @param molecule Input molecule
     * @param id ID to use
     */
    public static void doLookup(ECFPParameters ecfpParameters, Molecule molecule, String id) throws MDGeneratorException, IOException {
        // Calculate ECFP fingerprint ----------------------------------------------------------------------------------
        System.err.println("Calculate ECFP of the read molecule " + id);
        final ECFPGenerator gen = new ECFPGenerator();
        final ECFP fp = new ECFP(ecfpParameters);
        gen.generate(molecule, fp);
        final Set<Integer> features = fp.toIdentiferSet();

        System.err.println("Feature identifiers calculated:");
        System.err.println(features.toString());
        System.err.println();


        // Look up indicidual substructures for features ---------------------------------------------------------------
        final ECFPFeatureLookup lookup = new ECFPFeatureLookup(ecfpParameters);
        System.err.println("Look up identifiers");
        lookup.processMolecule(molecule);

        System.err.println("Print features");


        // Query all the features in the fingerprint from the lookup
        for (int i : features) {

            System.err.println("Look up feature for identifier " + i);

            // Multiple feature might be assigned to the hash due to collision
            // Also, same feature might be found centered on multiple atoms
            for (ECFPFeature feature : lookup.getFeaturesFromIdentifier(i)) {
                final int atomIndex = molecule.indexOf(feature.getCenterAtom());
                final int diameter  = feature.getDiameter();

                final String smarts = MolExporter.exportToFormat(feature.getSubstructure(), "SMARTS");

                System.err.println("    Atom: " + atomIndex + " Diameter: " + diameter + " SMARTS: " + smarts);

                // Construct output
                final StringBuilder o = new StringBuilder();
                o.append(smarts);    // append SMARTS
                o.append(' ');
                o.append(id); // append ID
                o.append(" ECFPID: ");
                o.append(feature.getIdentifier());   // append identifier
                o.append(" ECFPBIT: ");
                o.append(feature.getBitPosition()); // append bit position
                o.append(" DIA: ");                   // append diameters
                o.append(feature.getDiameter());
                o.append(" ATOM: ");                   // append central atom index
                o.append(molecule.indexOf(feature.getCenterAtom()));
                System.out.println(o.toString());

            }
        }
    }

    /**
     * Main method.
     *
     * @param args  Command line arguments. Launch with {@code -h} to print help
     *
     * @throws IOException              Re-thrown exception
     * @throws MDGeneratorException     Re-thrown exception
     */
    public static void main(String [] args) throws IOException, MDGeneratorException {

        final EcfpFeatureLookupParameters params = new EcfpFeatureLookupParameters();
        final JCommander jc = new JCommander(params, args);
        if (params.help) {
            jc.setProgramName(EcfpFeatureLookup.class.getName());
            jc.usage();
            return;
        }

        // Create ECFP parameters
        final ECFPParameters ecfpParameters;
        if (params.cfgfile == null) {
            System.err.println("Use default ECFP parameters");
            ecfpParameters = new ECFPParameters();
        } else {
            System.err.println("Read ECFP parameters from config file " + params.cfgfile);
            ecfpParameters = new ECFPParameters(new File(params.cfgfile));
        }

        System.err.println("Read all molecules from standard input.");

        final MolImporter molImporter = new MolImporter(System.in);
        Molecule molecule;
        int count = 0;
        while ((molecule = molImporter.read()) != null) {
            count ++;
            final String id = params.id(count, molecule);
            doLookup(ecfpParameters, molecule, id);
        }

        if (count == 0) {
            // No molecule read
            throw new IOException("No molecule read.");
        }
    }
}
