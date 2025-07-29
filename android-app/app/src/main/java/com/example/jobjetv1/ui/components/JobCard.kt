package com.example.jobjetv1.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jobjetv1.data.model.Job
import com.example.jobjetv1.utils.Constants
import com.example.jobjetv1.utils.FormatUtils
import com.example.jobjetv1.utils.ThemeUtils
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobCard(
    job: Job,
    onClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    isBookmarked: Boolean = false,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = ThemeUtils.BackgroundCard),
        shape = RoundedCornerShape(Constants.CARD_CORNER_RADIUS.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header row with title and bookmark
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Job title
                Text(
                    text = job.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = ThemeUtils.TextPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )

                // Bookmark button
                IconButton(
                    onClick = onBookmarkClick,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = if (isBookmarked) "Remove bookmark" else "Add bookmark",
                        tint = if (isBookmarked) ThemeUtils.PrimaryBlue else ThemeUtils.TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Company name
            Text(
                text = job.company,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = ThemeUtils.TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Salary
            if (job.salary.isNotEmpty()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Work,
                        contentDescription = null,
                        tint = ThemeUtils.SuccessColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = job.salary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = ThemeUtils.SuccessColor
                    )
                }
            }

            // Location
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = ThemeUtils.TextSecondary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = job.location,
                    fontSize = 13.sp,
                    color = ThemeUtils.TextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Posted time
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null,
                    tint = ThemeUtils.TextHint,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = FormatUtils.formatJobPostingTime(LocalDateTime.now().minusHours((0..72).random().toLong())),
                    fontSize = 12.sp,
                    color = ThemeUtils.TextHint
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Job type and category tags
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {                // Job type tag
                if (job.workType.isNotEmpty()) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = ThemeUtils.PrimaryBlueLight.copy(alpha = 0.1f),
                        modifier = Modifier
                    ) {
                        Text(
                            text = FormatUtils.formatJobType(job.workType),
                            fontSize = 11.sp,
                            color = ThemeUtils.PrimaryBlue,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                // Category tag
                if (job.category.isNotEmpty()) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = ThemeUtils.getCategoryColor(job.category).copy(alpha = 0.1f),
                        modifier = Modifier
                    ) {
                        Text(
                            text = job.category,
                            fontSize = 11.sp,
                            color = ThemeUtils.getCategoryColor(job.category),
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}