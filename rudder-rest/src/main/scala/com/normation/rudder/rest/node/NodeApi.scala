/*
*************************************************************************************
* Copyright 2013 Normation SAS
*************************************************************************************
*
* This file is part of Rudder.
*
* Rudder is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* In accordance with the terms of section 7 (7. Additional Terms.) of
* the GNU General Public License version 3, the copyright holders add
* the following Additional permissions:
* Notwithstanding to the terms of section 5 (5. Conveying Modified Source
* Versions) and 6 (6. Conveying Non-Source Forms.) of the GNU General
* Public License version 3, when you create a Related Module, this
* Related Module is not considered as a part of the work and may be
* distributed under the license agreement of your choice.
* A "Related Module" means a set of sources files including their
* documentation that, without modification of the Source Code, enables
* supplementary functions or services in addition to those offered by
* the Software.
*
* Rudder is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with Rudder.  If not, see <http://www.gnu.org/licenses/>.

*
*************************************************************************************
*/

package com.normation.rudder.rest.node

import net.liftweb.http.Req
import com.normation.rudder.rest.RestAPI
import com.normation.rudder.domain.nodes.NodeProperty
import com.normation.rudder.domain.policies.PolicyMode
import com.normation.rudder.service.user.UserService
import com.normation.rudder.domain.nodes.NodeState
import com.normation.rudder.AuthorizationType

trait NodeAPI extends RestAPI {
  val kind = "nodes"
  def userService : UserService

  override protected def checkSecure : PartialFunction[Req, Boolean] = {
    case Get(_,_) => userService.getCurrentUser.checkRights(AuthorizationType.Read("node"))
    case Post(_,_) | Put(_,_) | Delete(_,_) => val user = userService.getCurrentUser
      user.checkRights(AuthorizationType.Write("node")) || user.checkRights(AuthorizationType.Edit("node"))
    case _=> false

  }
}

case class RestNodeProperties(
    properties : Option[Seq[NodeProperty]]
)

case class RestNode (
    properties : Option[Seq[NodeProperty]]
  , policyMode : Option[Option[PolicyMode]]
  , state      : Option[NodeState]
)