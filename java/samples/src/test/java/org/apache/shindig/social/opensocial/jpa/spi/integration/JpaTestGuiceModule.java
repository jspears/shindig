/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package org.apache.shindig.social.opensocial.jpa.spi.integration;

import javax.persistence.EntityManager;

import net.oauth.OAuthConsumer;

import org.apache.shindig.auth.SecurityToken;
import org.apache.shindig.common.PropertiesModule;
import org.apache.shindig.gadgets.DefaultGuiceModule;
import org.apache.shindig.gadgets.admin.GadgetAdminModule;
import org.apache.shindig.gadgets.oauth.OAuthModule;
import org.apache.shindig.gadgets.oauth2.OAuth2MessageModule;
import org.apache.shindig.gadgets.oauth2.OAuth2Module;
import org.apache.shindig.gadgets.oauth2.handler.OAuth2HandlerModule;
import org.apache.shindig.gadgets.oauth2.persistence.sample.OAuth2PersistenceModule;
import org.apache.shindig.social.core.config.SocialApiGuiceModule;
import org.apache.shindig.social.core.oauth2.OAuth2DataService;
import org.apache.shindig.social.core.oauth2.OAuth2DataServiceImpl;
import org.apache.shindig.social.core.oauth2.OAuth2Service;
import org.apache.shindig.social.core.oauth2.OAuth2ServiceImpl;
import org.apache.shindig.social.opensocial.jpa.AccountDb;
import org.apache.shindig.social.opensocial.jpa.ActivityDb;
import org.apache.shindig.social.opensocial.jpa.AddressDb;
import org.apache.shindig.social.opensocial.jpa.BodyTypeDb;
import org.apache.shindig.social.opensocial.jpa.ListFieldDb;
import org.apache.shindig.social.opensocial.jpa.MediaItemDb;
import org.apache.shindig.social.opensocial.jpa.MessageDb;
import org.apache.shindig.social.opensocial.jpa.NameDb;
import org.apache.shindig.social.opensocial.jpa.OrganizationDb;
import org.apache.shindig.social.opensocial.jpa.PersonDb;
import org.apache.shindig.social.opensocial.jpa.UrlDb;
import org.apache.shindig.social.opensocial.jpa.spi.JPASocialModule;
import org.apache.shindig.social.opensocial.model.Account;
import org.apache.shindig.social.opensocial.model.Activity;
import org.apache.shindig.social.opensocial.model.Address;
import org.apache.shindig.social.opensocial.model.BodyType;
import org.apache.shindig.social.opensocial.model.ListField;
import org.apache.shindig.social.opensocial.model.MediaItem;
import org.apache.shindig.social.opensocial.model.Message;
import org.apache.shindig.social.opensocial.model.Name;
import org.apache.shindig.social.opensocial.model.Organization;
import org.apache.shindig.social.opensocial.model.Person;
import org.apache.shindig.social.opensocial.model.Url;
import org.apache.shindig.social.opensocial.oauth.OAuthDataStore;
import org.apache.shindig.social.opensocial.oauth.OAuthEntry;

import com.google.inject.AbstractModule;

/**
 * Provides component injection for tests
 * Injects Social API and JPA persistence guice modules
 *
 */
public class JpaTestGuiceModule extends AbstractModule {


  private EntityManager entityManager;

  JpaTestGuiceModule(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  /**
   * Bind entity manager, services and entities used by samples
   */
  @Override
  protected void configure() {
    install(new PropertiesModule());
    install(new GadgetAdminModule());
    install(new DefaultGuiceModule());
    install(new SocialApiGuiceModule());
    install(new OAuthModule());
    install(new OAuth2Module());
    install(new OAuth2PersistenceModule());
    install(new OAuth2MessageModule());
    install(new OAuth2HandlerModule());

    install(new JPASocialModule(entityManager));

    this.bind(OAuthDataStore.class).toInstance(new NullOAuthDataStore());
    bind(OAuth2Service.class).to(OAuth2ServiceImpl.class);
    bind(OAuth2DataService.class).to(OAuth2DataServiceImpl.class);
    // Entities
    this.bind(Activity.class).to(ActivityDb.class);
    this.bind(Account.class).to(AccountDb.class);
    this.bind(Address.class).to(AddressDb.class);
    this.bind(BodyType.class).to(BodyTypeDb.class);
    this.bind(ListField.class).to(ListFieldDb.class);
    this.bind(MediaItem.class).to(MediaItemDb.class);
    this.bind(Message.class).to(MessageDb.class);
    this.bind(Name.class).to(NameDb.class);
    this.bind(Organization.class).to(OrganizationDb.class);
    this.bind(Person.class).to(PersonDb.class);
    this.bind(Url.class).to(UrlDb.class);
  }

  private static class NullOAuthDataStore implements OAuthDataStore {
    public OAuthEntry getEntry(String oauthToken) {
      return null;
    }

    public OAuthConsumer getConsumer(String consumerKey) {
      return null;
    }


    public OAuthEntry convertToAccessToken(OAuthEntry entry) {
      throw new UnsupportedOperationException();
    }

    public void authorizeToken(OAuthEntry entry, String userId) {
      throw new UnsupportedOperationException();
    }

    public SecurityToken getSecurityTokenForConsumerRequest(String consumerKey, String userId) {
      throw new UnsupportedOperationException();
    }

    public void disableToken(OAuthEntry entry) {
      throw new UnsupportedOperationException();
    }

    public void removeToken(OAuthEntry entry) {
      throw new UnsupportedOperationException();
    }

    public OAuthEntry generateRequestToken(String consumerKey, String oauthVersion,
        String signedCallbackUrl) {
      return null;
    }
  }
}
