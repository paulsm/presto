/*
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
package com.facebook.presto.block;

import com.facebook.presto.tuple.Tuple;
import com.facebook.presto.tuple.TupleInfo;
import com.google.common.collect.ImmutableSortedMap;

import java.util.SortedMap;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

public class BlockCursorAssertions
{
    public static void assertAdvanceNextPosition(BlockCursor cursor)
    {
        assertTrue(cursor.advanceNextPosition());
    }

    public static void assertAdvanceToPosition(BlockCursor cursor, int position)
    {
        assertTrue(cursor.advanceToPosition(position));
    }

    public static void assertNextPosition(BlockCursor cursor, int position, Tuple tuple)
    {
        assertAdvanceNextPosition(cursor);
        assertCurrentValue(cursor, position, tuple);
    }

    public static void assertCurrentValue(BlockCursor cursor, int position, Tuple tuple)
    {
        TupleInfo tupleInfo = tuple.getTupleInfo();
        assertEquals(cursor.getTupleInfo(), tupleInfo);

        assertEquals(cursor.getTuple(), tuple);
        assertEquals(cursor.getPosition(), position);
        assertTrue(cursor.currentTupleEquals(tuple));

        for (int index = 0; index < tupleInfo.getFieldCount(); index++) {
            assertEquals(cursor.isNull(index), tuple.isNull(index));
            switch (tupleInfo.getTypes().get(index)) {
                case BOOLEAN:
                    assertEquals(cursor.getBoolean(index), tuple.getBoolean(index));
                    try {
                        cursor.getSlice(index);
                        fail("Expected IllegalStateException or UnsupportedOperationException");
                    }
                    catch (IllegalStateException | UnsupportedOperationException expected) {
                    }
                    try {
                        cursor.getSlice(index);
                        fail("Expected IllegalStateException or UnsupportedOperationException");
                    }
                    catch (IllegalStateException | UnsupportedOperationException expected) {
                    }
                    break;
                case FIXED_INT_64:
                    assertEquals(cursor.getLong(index), tuple.getLong(index));
                    try {
                        cursor.getDouble(index);
                        fail("Expected IllegalStateException or UnsupportedOperationException");
                    }
                    catch (IllegalStateException | UnsupportedOperationException expected) {
                    }
                    try {
                        cursor.getSlice(index);
                        fail("Expected IllegalStateException or UnsupportedOperationException");
                    }
                    catch (IllegalStateException | UnsupportedOperationException expected) {
                    }
                    break;
                case VARIABLE_BINARY:
                    assertEquals(cursor.getSlice(index), tuple.getSlice(index));
                    try {
                        cursor.getDouble(index);
                        fail("Expected IllegalStateException or UnsupportedOperationException");
                    }
                    catch (IllegalStateException | UnsupportedOperationException expected) {
                    }
                    try {
                        cursor.getLong(index);
                        fail("Expected IllegalStateException or UnsupportedOperationException");
                    }
                    catch (IllegalStateException | UnsupportedOperationException expected) {
                    }
                    break;
                case DOUBLE:
                    assertEquals(cursor.getDouble(index), tuple.getDouble(index));
                    try {
                        cursor.getSlice(index);
                        fail("Expected IllegalStateException or UnsupportedOperationException");
                    }
                    catch (IllegalStateException | UnsupportedOperationException expected) {
                    }
                    try {
                        cursor.getSlice(index);
                        fail("Expected IllegalStateException or UnsupportedOperationException");
                    }
                    catch (IllegalStateException | UnsupportedOperationException expected) {
                    }
                    break;
            }
        }
    }

    public static SortedMap<Integer, Tuple> toTuplesMap(BlockCursor cursor)
    {
        ImmutableSortedMap.Builder<Integer, Tuple> tuples = ImmutableSortedMap.naturalOrder();
        while (cursor.advanceNextPosition()) {
            tuples.put(cursor.getPosition(), cursor.getTuple());
        }
        return tuples.build();
    }
}
