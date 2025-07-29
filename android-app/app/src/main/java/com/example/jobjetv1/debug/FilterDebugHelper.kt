package com.example.jobjetv1.debug

import android.util.Log
import com.example.jobjetv1.repository.JobsRepository


// In FilterDebugHelper.kt
object FilterDebugHelper {
    fun getDebugCategoryInfo(): String { // Changed to return a String
        val allJobs = JobsRepository.allJobs
        val stringBuilder = StringBuilder()

        stringBuilder.append("=== All Jobs and Categories ===\n")
        allJobs.forEach { job ->
            stringBuilder.append("Job: '${job.title}' -> Category: '${job.category}'\n")
        }

        stringBuilder.append("=== Category Counts ===\n")
        val categoryCount = allJobs.groupBy { it.category }.mapValues { it.value.size }
        categoryCount.forEach { (category, count) ->
            stringBuilder.append("Category '$category': $count jobs\n")
        }

        stringBuilder.append("=== Testing Filter ===\n")
        val warehouseJobs = allJobs.filter { it.category == "Kho bãi" }
        stringBuilder.append("Kho bãi filter result: ${warehouseJobs.size} jobs\n")
        warehouseJobs.forEach { job ->
            stringBuilder.append("  - ${job.title}\n")
        }
        return stringBuilder.toString()
    }
}
