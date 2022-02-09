
package com.embeltech.meterreading.injection.annotations

import javax.inject.Scope

/**
 *
 * A scoping annotation to permit dependencies conform to the life of the
 * [ConfigPersistentComonent]
 */

@MustBeDocumented
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ConfigPersistent