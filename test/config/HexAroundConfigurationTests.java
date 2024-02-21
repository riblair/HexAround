/*
 * ******************************************************************************
 *  This files was developed for CS4233: Object-Oriented Analysis & Design.
 *  The course was taken at Worcester Polytechnic Institute.
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  * Copyright ©2016-2017 Gary F. Pollice
 *  ******************************************************************************
 *
 */

package config;

import hexaround.config.*;
import org.junit.jupiter.api.*;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class HexAroundConfigurationTests {
    @Test
    void simpleTest() throws IOException {
        String hgcFile = "testConfigurations/FirstConfiguration.hgc";
        HexAroundConfigurationMaker maker = new HexAroundConfigurationMaker(hgcFile);
        GameConfiguration gc = maker.makeConfiguration();
        assertTrue(true);
        System.out.println(gc.toString());
    }
}
