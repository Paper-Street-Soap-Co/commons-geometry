/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.geometry.euclidean.twod;

import org.apache.commons.geometry.core.RegionLocation;
import org.apache.commons.geometry.core.Transform;
import org.apache.commons.geometry.core.partitioning.Split;

/** Class representing a portion of a line in 2D Euclidean space that starts at infinity and
 * continues in the direction of the line up to a single end point. This is equivalent to taking a
 * {@link Ray} and reversing the line direction.
 *
 * <p>Instances of this class are guaranteed to be immutable.</p>
 * @see Ray
 * @see Lines
 */
public final class ReverseRay extends LineConvexSubset {

    /** The abscissa of the endpoint. */
    private final double end;

    /** Construct a new instance from the given line and end point. The end point is projected onto
     * the line. No validation is performed.
     * @param line line for the instance
     * @param endPoint end point for the instance
     */
    ReverseRay(final Line line, final Vector2D endPoint) {
        this(line, line.abscissa(endPoint));
    }

    /** Construct a new instance from the given line and 1D end location. No validation is performed.
     * @param line line for the instance
     * @param end end location for the instance
     */
    ReverseRay(final Line line, final double end) {
        super(line);

        this.end = end;
    }

    /** {@inheritDoc}
     *
     * <p>This method always returns {@code false}.</p>
     */
    @Override
    public boolean isFull() {
        return false;
    }

    /** {@inheritDoc}
    *
    * <p>This method always returns {@code true}.</p>
    */
    @Override
    public boolean isInfinite() {
        return true;
    }

    /** {@inheritDoc}
    *
    * <p>This method always returns {@code false}.</p>
    */
    @Override
    public boolean isFinite() {
        return false;
    }

    /** {@inheritDoc}
    *
    * <p>This method always returns {@link Double#POSITIVE_INFINITY}.</p>
    */
    @Override
    public double getSize() {
        return Double.POSITIVE_INFINITY;
    }

    /** {@inheritDoc}
    *
    * <p>This method always returns {@code null}.</p>
    */
    @Override
    public Vector2D getStartPoint() {
        return null;
    }

    /** {@inheritDoc}
    *
    * <p>This method always returns {@link Double#NEGATIVE_INFINITY}.</p>
    */
    @Override
    public double getSubspaceStart() {
        return Double.NEGATIVE_INFINITY;
    }

    /** {@inheritDoc} */
    @Override
    public Vector2D getEndPoint() {
        return getLine().toSpace(end);
    }

    /** {@inheritDoc} */
    @Override
    public double getSubspaceEnd() {
        return end;
    }

    /** {@inheritDoc} */
    @Override
    public ReverseRay transform(final Transform<Vector2D> transform) {
        final Line tLine = getLine().transform(transform);
        final Vector2D tEnd = transform.apply(getEndPoint());

        return new ReverseRay(tLine, tEnd);
    }

    /** {@inheritDoc} */
    @Override
    public Ray reverse() {
        return new Ray(getLine().reverse(), -end);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName())
            .append("[direction= ")
            .append(getLine().getDirection())
            .append(", endPoint= ")
            .append(getEndPoint())
            .append(']');

        return sb.toString();
    }

    /** {@inheritDoc} */
    @Override
    RegionLocation classifyAbscissa(double abscissa) {
        int cmp = getPrecision().compare(abscissa, end);
        if (cmp < 0) {
            return RegionLocation.INSIDE;
        } else if (cmp == 0) {
            return RegionLocation.BOUNDARY;
        }

        return RegionLocation.OUTSIDE;
    }

    /** {@inheritDoc} */
    @Override
    double closestAbscissa(double abscissa) {
        return Math.min(end, abscissa);
    }

    /** {@inheritDoc} */
    @Override
    protected Split<LineConvexSubset> splitOnIntersection(final Line splitter, final Vector2D intersection) {

        final Line line = getLine();
        final double splitAbscissa = line.abscissa(intersection);

        LineConvexSubset low = null;
        LineConvexSubset high = null;

        int cmp = getPrecision().compare(splitAbscissa, end);
        if (cmp < 0) {
            low = new ReverseRay(line, splitAbscissa);
            high = new Segment(line, splitAbscissa, end);
        } else {
            low = this;
        }

        return createSplitResult(splitter, low, high);
    }
}
