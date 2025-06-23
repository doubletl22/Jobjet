package com.example.jobjetv1.utils

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalInspectionMode
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

object PerformanceUtils {
    
    /**
     * Debug function to measure composition time
     */
    @Composable
    fun MeasureComposition(
        tag: String,
        content: @Composable () -> Unit
    ) {
        if (LocalInspectionMode.current) {
            content()
            return
        }
        
        val startTime = remember { System.currentTimeMillis() }
        content()
        val endTime = System.currentTimeMillis()
          LaunchedEffect(startTime, endTime) {
            // Debug mode check - replace with true for debug builds
            if (true) {
                println("Composition [$tag]: ${endTime - startTime}ms")
            }
        }
    }
    
    /**
     * Debounce function for search inputs
     */
    @Composable
    fun <T> rememberDebounced(
        value: T,
        delayMillis: Long = Constants.SEARCH_DEBOUNCE_DELAY
    ): T {
        var debouncedValue by remember { mutableStateOf(value) }
        
        LaunchedEffect(value) {
            delay(delayMillis)
            debouncedValue = value
        }
        
        return debouncedValue
    }
    
    /**
     * Memoization helper for expensive calculations
     */
    @Composable
    fun <T, R> rememberCalculation(
        input: T,
        calculation: (T) -> R
    ): R {
        return remember(input) {
            calculation(input)
        }
    }
    
    /**
     * Lazy loading state for large datasets
     */
    @Composable
    fun <T> rememberLazyLoader(
        items: List<T>,
        pageSize: Int = Constants.JOBS_PER_PAGE,
        initialLoadSize: Int = pageSize
    ): State<List<T>> {
        var loadedItems by remember { mutableStateOf(items.take(initialLoadSize)) }
        var currentPage by remember { mutableStateOf(1) }
        
        LaunchedEffect(items) {
            loadedItems = items.take(initialLoadSize)
            currentPage = 1
        }
        
        return remember { 
            derivedStateOf { loadedItems }
        }
    }
    
    /**
     * Memory optimization for image loading
     */
    fun getOptimalImageSize(screenWidth: Int, screenHeight: Int): Pair<Int, Int> {
        val maxWidth = screenWidth / 2
        val maxHeight = screenHeight / 4
        return Pair(maxWidth, maxHeight)
    }
    
    /**
     * Background task executor with coroutine scope
     */
    class BackgroundTaskExecutor {
        private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        
        fun <T> executeAsync(
            task: suspend () -> T,
            onSuccess: (T) -> Unit = {},
            onError: (Throwable) -> Unit = {}
        ) {
            scope.launch {
                try {
                    val result = task()
                    withContext(Dispatchers.Main) {
                        onSuccess(result)
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        onError(e)
                    }
                }
            }
        }
        
        fun cleanup() {
            scope.cancel()
        }
    }
    
    /**
     * Cache utility for frequently accessed data
     */
    class SimpleCache<K, V>(private val maxSize: Int = 100) {
        private val cache = mutableMapOf<K, CacheEntry<V>>()
        private val accessOrder = mutableListOf<K>()
        
        data class CacheEntry<V>(
            val value: V,
            val timestamp: Long = System.currentTimeMillis()
        )
        
        fun get(key: K): V? {
            val entry = cache[key]
            if (entry != null) {
                // Move to end (most recently used)
                accessOrder.remove(key)
                accessOrder.add(key)
                return entry.value
            }
            return null
        }
        
        fun put(key: K, value: V) {
            if (cache.size >= maxSize && !cache.containsKey(key)) {
                // Remove least recently used
                val lru = accessOrder.removeFirstOrNull()
                if (lru != null) {
                    cache.remove(lru)
                }
            }
            
            cache[key] = CacheEntry(value)
            accessOrder.remove(key)
            accessOrder.add(key)
        }
        
        fun clear() {
            cache.clear()
            accessOrder.clear()
        }
        
        fun size(): Int = cache.size
    }
    
    /**
     * Network request optimization
     */
    suspend fun <T> withRetry(
        times: Int = 3,
        initialDelay: Long = 100,
        maxDelay: Long = 1000,
        factor: Double = 2.0,
        block: suspend () -> T
    ): T {
        var currentDelay = initialDelay
        repeat(times - 1) {
            try {
                return block()
            } catch (e: Exception) {
                delay(currentDelay)
                currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
            }
        }
        return block() // Last attempt
    }
    
    /**
     * Analytics tracking for performance monitoring
     */
    object Analytics {
        private val events = mutableListOf<AnalyticsEvent>()
        
        data class AnalyticsEvent(
            val name: String,
            val timestamp: Long,
            val duration: Long?,
            val properties: Map<String, Any>
        )
          fun trackEvent(
            name: String,
            properties: Map<String, Any> = emptyMap()
        ) {
            // Debug mode check - replace with true for debug builds
            if (true) {
                events.add(
                    AnalyticsEvent(
                        name = name,
                        timestamp = System.currentTimeMillis(),
                        duration = null,
                        properties = properties
                    )
                )
                println("Analytics: $name - $properties")
            }
        }
        
        suspend fun trackTimedEvent(
            name: String,
            properties: Map<String, Any> = emptyMap(),
            block: suspend () -> Unit
        ) {            val duration = measureTimeMillis {
                block()
            }
            
            // Debug mode check - replace with true for debug builds
            if (true) {
                events.add(
                    AnalyticsEvent(
                        name = name,
                        timestamp = System.currentTimeMillis(),
                        duration = duration,
                        properties = properties + ("duration_ms" to duration)
                    )
                )
                println("Analytics: $name - ${duration}ms - $properties")
            }
        }
        
        fun getEvents(): List<AnalyticsEvent> = events.toList()
        
        fun clearEvents() {
            events.clear()
        }
    }
}
