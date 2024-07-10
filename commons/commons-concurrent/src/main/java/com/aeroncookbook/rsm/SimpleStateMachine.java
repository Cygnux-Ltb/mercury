/*
 * Copyright 2019-2023 Adaptive Financial Consulting Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aeroncookbook.rsm;

import java.util.ArrayList;
import java.util.List;

public class SimpleStateMachine
{
    private int currentValue = 0;
    private final List<EventListener> eventListeners = new ArrayList<>();

    public void addListener(final EventListener eventListener)
    {
        eventListeners.add(eventListener);
    }

    public void add(final AddCommand addCommand)
    {
        currentValue += addCommand.value;
        notifyListeners();
    }

    public void multiply(final MultiplyCommand multiplyCommand)
    {
        currentValue *= multiplyCommand.value;
        notifyListeners();
    }

    public void set(final SetCommand setCommand)
    {
        currentValue = setCommand.value;
        notifyListeners();
    }

    public void snapshot(final SnapshotCommand snapshotCommand)
    {
        notifyListeners();
    }

    private void notifyListeners()
    {
        final NewValueEvent newValueEvent = new NewValueEvent();
        newValueEvent.currentValue = currentValue;
        for (final EventListener eventListener : eventListeners)
        {
            eventListener.newValue(newValueEvent);
        }
    }
}

