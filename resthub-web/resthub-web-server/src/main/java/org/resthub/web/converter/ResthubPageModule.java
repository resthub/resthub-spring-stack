package org.resthub.web.converter;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.resthub.web.PageResponse;
import org.springframework.data.domain.Page;


/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Jackson module.
 * Handles Spring-data's Page/PageImpl to RESThub's PageResponse mapping; in two steps:
 * <ul>
 *   <li>use PageResponse as a concrete type for serializing Page interface</li>
 *   <li>use PageResponse's annotations for serialization, using mixinannotations</li>
 * </ul>
 *
 */
public class ResthubPageModule extends SimpleModule {

    public ResthubPageModule() {
        super("ResthubPageModule");
    }

    @Override
    public void setupModule(SetupContext context) {
        context.setMixInAnnotations(Page.class, PageResponse.class);
        this.addAbstractTypeMapping(Page.class, PageResponse.class);
    }
}
