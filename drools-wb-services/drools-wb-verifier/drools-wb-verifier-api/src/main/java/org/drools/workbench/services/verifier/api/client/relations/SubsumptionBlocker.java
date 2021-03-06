/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
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
 */

package org.drools.workbench.services.verifier.api.client.relations;

import org.drools.workbench.services.verifier.api.client.index.keys.UUIDKey;
import org.drools.workbench.services.verifier.api.client.maps.InspectorList;
import org.drools.workbench.services.verifier.api.client.maps.util.HasUUID;
import org.kie.soup.commons.validation.PortablePreconditions;

public class SubsumptionBlocker
        extends Relation<SubsumptionBlocker> {

    public static final SubsumptionBlocker EMPTY = new SubsumptionBlocker();

    private final InspectorList list;
    private final HasUUID item;

    private SubsumptionBlocker() {
        super(null);
        list = null;
        item = null;
    }

    public SubsumptionBlocker(final InspectorList list,
                              final HasUUID item) {
        super(null);
        this.list = PortablePreconditions.checkNotNull("list",
                                                       list);
        this.item = PortablePreconditions.checkNotNull("item",
                                                       item);
    }

    public SubsumptionBlocker(final InspectorList list,
                              final HasUUID item,
                              final SubsumptionBlocker origin) {
        super(origin);
        this.list = PortablePreconditions.checkNotNull("list",
                                                       list);
        this.item = PortablePreconditions.checkNotNull("item",
                                                       item);
    }

    private HasUUID getItem() {
        return item;
    }

    private InspectorList getList() {
        return list;
    }

    public boolean foundIssue() {
        return item != null;
    }

    @Override
    public UUIDKey otherUUID() {
        return item.getUuidKey();
    }

    @Override
    public boolean doesRelationStillExist() {
        if (origin != null
                && stillContainsBlockingItem(getOrigin().getItem())) {
            return SubsumptionResolver.isSubsumedByAnObjectInThisList(getOrigin().getList(),
                                                                      getOrigin().getItem()).foundIssue();
        } else {
            return false;
        }
    }

    private boolean stillContainsBlockingItem(final HasUUID item) {
        if (this.item instanceof InspectorList) {
            return ((InspectorList) this.item).contains(item);
        } else {
            if (parent != null) {
                return parent.stillContainsBlockingItem(item);
            } else {
                return false;
            }
        }
    }
}
