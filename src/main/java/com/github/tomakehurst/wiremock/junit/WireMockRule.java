/*
 * Copyright (C) 2011 Thomas Akehurst
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.tomakehurst.wiremock.junit;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.Options;
import org.junit.rules.MethodRule;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class WireMockRule implements MethodRule, TestRule {

    private final Options options;

    public WireMockRule(Options options) {
        this.options = options;
    }

    public WireMockRule(int port) {
		this(wireMockConfig().port(port));
	}

    public WireMockRule(int port, Integer httpsPort) {
        this(wireMockConfig().port(port).httpsPort(httpsPort));
    }
	
	public WireMockRule() {
		this(wireMockConfig());
	}

    @Override
    public Statement apply(final Statement base, Description description) {
        return apply(base, null, null);
    }

	@Override
	public Statement apply(final Statement base, FrameworkMethod method, Object target) {
		return new Statement() {

			@Override
			public void evaluate() throws Throwable {
				WireMockServer wireMockServer = new WireMockServer(options);
				wireMockServer.start();
				WireMock.configureFor("localhost", options.portNumber());
				try {
                    base.evaluate();
                } finally {
                    wireMockServer.stop();
                }
			}
			
		};
	}

}
