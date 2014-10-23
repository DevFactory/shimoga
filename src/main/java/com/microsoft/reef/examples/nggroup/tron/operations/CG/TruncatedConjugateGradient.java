/**
 * Copyright (C) 2014 Microsoft Corporation
 */
package com.microsoft.reef.examples.nggroup.tron.operations.CG;

import com.microsoft.reef.examples.nggroup.tron.math.DenseVector;
import com.microsoft.reef.examples.nggroup.tron.math.Vector;
import org.apache.reef.exception.evaluator.NetworkException;
import org.apache.reef.io.network.util.Pair;

/**
 *
 */
public class TruncatedConjugateGradient {

  /**
   *
   */
  private static final int SIZE = 4;

  private TruncatedConjugateGradient() {
  }

  /**
   * Solve Ax = b by min (Ax - b)^2 s.t ||x|| <= truncationBound
   * <p/>
   * Here we do not specify A explictly. Instead we expect an
   * object called CGDirectionProjector that can compute Ax
   * <p/>
   * If truncation is not needed set truncationBound = Double.PositiveInfinty
   * <p/>
   * Tolerance is relative and is defined more from the perspective of
   * Trust Region algorithm
   *
   * @param projector       - The object that computes Ax
   * @param projection      - b
   * @param truncationBound
   * @param tolerance       - ||Ax - b|| <= tolerance * ||b||
   * @param maxIterations
   * @return
   * @throws InterruptedException
   * @throws NetworkException
   */
  public static Pair<Vector, Vector> compute(final CGDirectionProjector projector,
                                             final Vector projection,
                                             final double truncationBound,
                                             final double tolerance,
                                             final int maxIterations) throws NetworkException, InterruptedException {
    final int dim = projection.size();
    final Vector ModelStep = new DenseVector(dim);
    final Vector Residual = new DenseVector(projection);
    final Vector ConjugateDir = new DenseVector(projection);
    final Vector HessianTimesConjugateDir = new DenseVector(dim);

    final double InitialResidualNorm = projection.norm2();
    double ResidualNorm = InitialResidualNorm;
    int CurrentIter = 0;

    do {

      projector.project(ConjugateDir, HessianTimesConjugateDir);
      double alpha = ResidualNorm * ResidualNorm / HessianTimesConjugateDir.dot(ConjugateDir);
      ModelStep.multAdd(alpha, ConjugateDir);
      System.err.println("alpha=" + alpha);
      /*** For trunation ***/
      double ModelStepNorm = ModelStep.norm2();
      if (ModelStepNorm > truncationBound) {
        ModelStep.multAdd(-alpha, ConjugateDir);
        ModelStepNorm = ModelStep.norm2();
        final double ModelStepNorm2 = ModelStepNorm * ModelStepNorm;
        final double ConjugateDirectionNorm2 = ConjugateDir.norm2Sqr();
        final double AngleCGDirModelStep = ModelStep.dot(ConjugateDir);
        final double truncationBound2 = truncationBound * truncationBound;
        final double discriminant = Math.sqrt(AngleCGDirModelStep * AngleCGDirModelStep -
            ConjugateDirectionNorm2 * (ModelStepNorm2 - truncationBound2));
        if (AngleCGDirModelStep > 0) {
          alpha = (truncationBound2 - ModelStepNorm2) / (discriminant + AngleCGDirModelStep);
        } else {
          alpha = (discriminant - AngleCGDirModelStep) / ConjugateDirectionNorm2;
        }
        System.err.println("Truncation: alpha=" + alpha);
        System.err.println(new Pair<>(ModelStep, Residual));
        ModelStep.multAdd(alpha, ConjugateDir);
        Residual.multAdd(-alpha, HessianTimesConjugateDir);
        break;
      }
      /*********************/

      double beta = ResidualNorm;
      Residual.multAdd(-alpha, HessianTimesConjugateDir);
      ResidualNorm = Residual.norm2();
      beta = ResidualNorm / beta;
      beta *= beta;

      ConjugateDir.scale(beta);
      ConjugateDir.add(Residual);
      System.err.println("Current Iter: " + CurrentIter + " InitRN: " + InitialResidualNorm + " ResNorm: " + ResidualNorm);
      System.err.println(new Pair<>(ModelStep, Residual));

    } while (++CurrentIter < maxIterations && ResidualNorm > tolerance * InitialResidualNorm);

    return new Pair<>(ModelStep, Residual);
  }

  public static void main(final String[] args) throws NetworkException, InterruptedException {
    final Vector[] A = new Vector[4];
    A[0] = new DenseVector(new double[]{5, 13, 4, 11});
    A[1] = new DenseVector(new double[]{13, 34, 1, 7});
    A[2] = new DenseVector(new double[]{1, 2, 3, 4});
    A[3] = new DenseVector(new double[]{2, 4, 1, 9});
    final Vector result = new DenseVector(new double[]{1, 3, 3, 1});
    final Vector projection = new DenseVector(SIZE);
    final Vector[] AdashA = new Vector[SIZE];
    for (int i = 0; i < A.length; i++) {
      AdashA[i] = new DenseVector(SIZE);
      for (int k = 0; k < SIZE; k++) {
        AdashA[i].set(k, A[i].dot(A[k]));
      }
//      System.out.println(AdashA[i]);
      projection.set(i, AdashA[i].dot(result));
    }
//    System.out.println(projection);

    System.out.println(TruncatedConjugateGradient.compute(new TestProjector(AdashA),
        projection,
        4,
        1e-6,
        200));
    System.out.println("Expected Result: " + result);
  }
}
