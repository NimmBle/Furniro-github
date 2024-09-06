package com.example.service.impl

import com.example.model.Category
import com.example.model.CategoryDTO
import com.example.repository.CategoryRepository
import com.example.service.AzureBlobService
import com.example.service.CategoryService
import io.ktor.http.content.*

class CategoryServiceImpl(
    private val categoryRepository: CategoryRepository = CategoryRepository(),
    private val azureBlobService: AzureBlobService
) : CategoryService {
    override fun getAllCategories(): List<CategoryDTO> =
        categoryRepository.getAll().map { it.toDto() }

    override fun getByName(name: String): CategoryDTO =
        categoryRepository.getCategoryByName(name)?.toDto()
            ?: throw IllegalArgumentException("Category with name $name does not exist")

    override suspend fun createCategory(multipart: MultiPartData) {

        val (name, imageUrl) = uploadImage(multipart)

        val category = CategoryDTO(null, name, imageUrl);

        if (categoryRepository.getCategoryByName(category.name) != null) {
            throw IllegalArgumentException("Category with name ${category.name} already exists")
        }

        categoryRepository.addCategory(category)
    }

    override suspend fun updateCategory(oldName: String, multipart: MultiPartData) {
        if (categoryRepository.getCategoryByName(oldName) == null) {
            throw IllegalArgumentException("Category with name $oldName does not exist")
        }

        val (newName, imageUrl) = uploadImage(multipart)

        val updatedCategory = CategoryDTO(null, newName, imageUrl)

        categoryRepository.editCategory(oldName, updatedCategory)
    }

    override fun updateCategory(oldName: String, newName: String) {
        val categoryDTO = categoryRepository.getCategoryByName(oldName)
            ?: throw IllegalArgumentException("Category with name $oldName does not exist")


        val updatedCategory = CategoryDTO(categoryDTO.id.value, newName, categoryDTO.coverPhotoUrl)
        categoryRepository.editCategory(oldName, updatedCategory)
    }

    override fun deleteCategory(name: String) {
        if (categoryRepository.getCategoryByName(name) == null) {
            throw IllegalArgumentException("Category with name $name does not exist")
        }
        categoryRepository.deleteCategory(name)
    }

    private fun Category.toDto() = CategoryDTO(this.id.value, this.name, this.coverPhotoUrl)

    private suspend fun uploadImage(multipart: MultiPartData): Pair<String, String> {

        var name: String? = null
        var image: PartData.FileItem? = null

        multipart.forEachPart { part ->
            when (part) {
                is PartData.FormItem -> {
                    if (part.name == "name") {
                        name = part.value
                    }
                }
                is PartData.FileItem -> {
                    image = part
                }
                else -> Unit
            }
        }

        if (image == null) {
            throw IllegalArgumentException("Image is null")
        }

        if (name == null) {
            throw IllegalArgumentException("Name is null")
        }

        val imageUrl = azureBlobService.uploadImage(image!!)

        return Pair(name!!, imageUrl)
    }
}