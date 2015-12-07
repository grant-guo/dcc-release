/*
 * Copyright (c) 2015 The Ontario Institute for Cancer Research. All rights reserved.                             
 *                                                                                                               
 * This program and the accompanying materials are made available under the terms of the GNU Public License v3.0.
 * You should have received a copy of the GNU General Public License along with                                  
 * this program. If not, see <http://www.gnu.org/licenses/>.                                                     
 *                                                                                                               
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY                           
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES                          
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT                           
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,                                
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED                          
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;                               
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER                              
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN                         
 * ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.icgc.dcc.release.job.join.function;

import static org.icgc.dcc.common.core.model.FieldNames.LoaderFieldNames.CONSEQUENCE_ARRAY_NAME;
import static org.icgc.dcc.common.core.model.FieldNames.LoaderFieldNames.GENE_ID;
import static org.icgc.dcc.common.core.model.FieldNames.LoaderFieldNames.PROJECT_ID;
import static org.icgc.dcc.common.core.model.FieldNames.LoaderFieldNames.TRANSCRIPT_ID;
import static org.icgc.dcc.common.core.model.FieldNames.SubmissionFieldNames.SUBMISSION_ANALYZED_SAMPLE_ID;
import static org.icgc.dcc.common.core.model.FieldNames.SubmissionFieldNames.SUBMISSION_GENE_AFFECTED;
import static org.icgc.dcc.common.core.model.FieldNames.SubmissionFieldNames.SUBMISSION_OBSERVATION_ANALYSIS_ID;
import static org.icgc.dcc.common.core.model.FieldNames.SubmissionFieldNames.SUBMISSION_TRANSCRIPT_AFFECTED;
import static org.icgc.dcc.release.core.util.FieldNames.JoinFieldNames.MUTATION_ID;

import java.util.Collection;
import java.util.Set;

import lombok.val;

import org.apache.spark.api.java.function.Function;

import scala.Tuple2;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

public class CreateOccurrenceFromSecondary implements
    Function<Tuple2<String, Tuple2<ObjectNode, Optional<Iterable<ObjectNode>>>>, ObjectNode> {

  private static final Set<String> REMOVE_CONSEQUENCE_FIELDS = ImmutableSet.of(PROJECT_ID, MUTATION_ID,
      SUBMISSION_OBSERVATION_ANALYSIS_ID, SUBMISSION_ANALYZED_SAMPLE_ID);

  @Override
  public ObjectNode call(Tuple2<String, Tuple2<ObjectNode, Optional<Iterable<ObjectNode>>>> tuple) throws Exception {
    val primary = tuple._2._1;
    val secondaries = tuple._2._2;

    primary.putPOJO(CONSEQUENCE_ARRAY_NAME, createConsequences(secondaries));

    return primary;
  }

  private static Collection<? extends JsonNode> createConsequences(Optional<Iterable<ObjectNode>> secondaries) {
    val consequences = Sets.<JsonNode> newHashSet();

    if (secondaries.isPresent()) {
      for (val secondary : secondaries.get()) {
        secondary.remove(REMOVE_CONSEQUENCE_FIELDS);
        enrichConsequence(secondary);
        consequences.add(secondary);
      }
    }

    return consequences;
  }

  private static void enrichConsequence(ObjectNode secondary) {
    secondary.set(GENE_ID, secondary.remove(SUBMISSION_GENE_AFFECTED));
    secondary.set(TRANSCRIPT_ID, secondary.remove(SUBMISSION_TRANSCRIPT_AFFECTED));
  }

}
