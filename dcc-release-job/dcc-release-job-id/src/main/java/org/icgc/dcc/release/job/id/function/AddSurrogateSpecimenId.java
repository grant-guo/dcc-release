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
package org.icgc.dcc.release.job.id.function;

import static org.icgc.dcc.common.core.model.FieldNames.SubmissionFieldNames.SUBMISSION_SPECIMEN_ID;
import lombok.NonNull;
import lombok.val;

import org.icgc.dcc.common.core.model.FieldNames.IdentifierFieldNames;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class AddSurrogateSpecimenId extends AddSurrogateId {

  public AddSurrogateSpecimenId(@NonNull String identifierUrl, @NonNull String releaseName) {
    super(identifierUrl, releaseName);
  }

  @Override
  public ObjectNode call(ObjectNode row) throws Exception {
    val submittedSpecimenId = row.get(SUBMISSION_SPECIMEN_ID).textValue();
    val submittedProjectId = getSubmittedProjectId(row);
    val specimenId = client().getSpecimenId(submittedSpecimenId, submittedProjectId);

    row.put(IdentifierFieldNames.SURROGATE_SPECIMEN_ID, specimenId);

    return row;
  }

}