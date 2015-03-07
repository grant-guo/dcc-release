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
package org.icgc.dcc.etl2.job.id.core;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.val;

import org.icgc.dcc.etl2.core.job.Job;
import org.icgc.dcc.etl2.core.job.JobContext;
import org.icgc.dcc.etl2.core.job.JobType;
import org.icgc.dcc.etl2.core.task.TaskExecutor;
import org.icgc.dcc.etl2.job.id.task.SurrogateDonorIdTask;
import org.icgc.dcc.etl2.job.id.task.SurrogateMutationIdTask;
import org.icgc.dcc.etl2.job.id.task.SurrogateSampleIdTask;
import org.icgc.dcc.etl2.job.id.task.SurrogateSpecimenIdTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class IdJob implements Job {

  /**
   * Constants.
   */
  @Value("${dcc.identifier.url}")
  private String identifierUrl;

  /**
   * Dependencies.
   */
  @Autowired
  private TaskExecutor executor;

  @Override
  public JobType getType() {
    return JobType.ID;
  }

  @Override
  @SneakyThrows
  public void execute(@NonNull JobContext jobContext) {
    val releaseName = jobContext.getReleaseName();

    executor.execute(jobContext, new SurrogateDonorIdTask(identifierUrl, releaseName));
    executor.execute(jobContext, new SurrogateSpecimenIdTask(identifierUrl, releaseName));
    executor.execute(jobContext, new SurrogateSampleIdTask(identifierUrl, releaseName));
    executor.execute(jobContext, new SurrogateMutationIdTask(identifierUrl, releaseName));
  }

}
