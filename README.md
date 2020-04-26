# Authorization manager SDK Kotlin

![](https://github.com/authorization-manager/sdk-kotlin/workflows/Push%20to%20master%20CI/badge.svg)

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=authorization-manager_sdk-kotlin&metric=alert_status)](https://sonarcloud.io/dashboard?id=authorization-manager_sdk-kotlin)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=authorization-manager_sdk-kotlin&metric=bugs)](https://sonarcloud.io/dashboard?id=authorization-manager_sdk-kotlin)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=authorization-manager_sdk-kotlin&metric=code_smells)](https://sonarcloud.io/dashboard?id=authorization-manager_sdk-kotlin)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=authorization-manager_sdk-kotlin&metric=coverage)](https://sonarcloud.io/dashboard?id=authorization-manager_sdk-kotlin)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=authorization-manager_sdk-kotlin&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=authorization-manager_sdk-kotlin)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=authorization-manager_sdk-kotlin&metric=ncloc)](https://sonarcloud.io/dashboard?id=authorization-manager_sdk-kotlin)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=authorization-manager_sdk-kotlin&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=authorization-manager_sdk-kotlin)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=authorization-manager_sdk-kotlin&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=authorization-manager_sdk-kotlin)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=authorization-manager_sdk-kotlin&metric=security_rating)](https://sonarcloud.io/dashboard?id=authorization-manager_sdk-kotlin)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=authorization-manager_sdk-kotlin&metric=sqale_index)](https://sonarcloud.io/dashboard?id=authorization-manager_sdk-kotlin)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=authorization-manager_sdk-kotlin&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=authorization-manager_sdk-kotlin)


## Key concepts

- User: person that use the service/product/system where this library is used.
- Permission: ability to access information or perform an action. Permissions have a *description*
- Role: set of permissions. Roles have a description.

## Rules

- Users, permissions and roles have to be uniquely identifiable.
- A user can be appointed as many roles.
- A role can have many assigned permissions.
- A user has a permission if he is appointed as some role that has that permission

## Features

- Create resource.
- Get a resource.
- Update resource attributes
- Delete a resource.

- Create permission.
- Update permission name.
- Update permission description.
- Get a permission.
- Delete a permission.
- Assign a permission to a resource.
- Assign a set of permissions to a resource.
- Withdraw a permission from a resource.
- Withdraw a set of permissions from a resource.

- Assign a permission to a role.
- Assign a set of permissions to a role.
- Withdraw a permission from a role.
- Withdraw a set of permissions from a role.
- Respond whether a permission is assigned to a role.

- Create user
- Get user
- Update user attributes
- Delete a user
- Appoint user as [role].
- Withdraw user from [role].
- Respond whether a permission is assigned to a user.
