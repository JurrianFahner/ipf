/*
 * Copyright 2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.openehealth.ipf.modules.hl7.kotlin.parser

import ca.uhn.hl7v2.parser.EncodingCharacters

/**
 * @author Christian Ohr
 * @since 3.5
 */
object DefaultEscaping: ca.uhn.hl7v2.parser.DefaultEscaping() {

    override fun escape(text: String, encChars: EncodingCharacters): String {
        val fixed = text.replace("\r", "\\X000d\\")
        return super.escape(fixed, encChars)
    }
}