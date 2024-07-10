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

package com.aeroncookbook.lamport;

public class Message
{
    private final long messageTime;
    private final String message;

    public Message(final long messageTime, final String message)
    {
        this.messageTime = messageTime;
        this.message = message;
    }

    public long getTime()
    {
        return messageTime;
    }

    public String getMsg()
    {
        return message;
    }
}
